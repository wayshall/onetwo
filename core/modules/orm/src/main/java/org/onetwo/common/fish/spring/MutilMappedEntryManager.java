package org.onetwo.common.fish.spring;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.fish.exception.JFishNoMappedEntryException;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.orm.JFishMappedEntry;
import org.onetwo.common.fish.orm.MappedEntryBuilder;
import org.onetwo.common.fish.orm.MappedEntryManager;
import org.onetwo.common.fish.orm.MappedEntryManagerListener;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.JFishList;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;

public class MutilMappedEntryManager implements MappedEntryBuilder, MappedEntryManager, InitializingBean, Ordered, ApplicationContextAware {
	
//	static enum EntryBuildEvent {
//		beforeBuild,
//		afterBuilt,
//		afterAllEntryHasBuilt
//	}

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private Map<String, JFishMappedEntry> entryCache = new ConcurrentHashMap<String, JFishMappedEntry>();
	private List<MappedEntryBuilder> mapedEntryBuilders;
	private MappedEntryListenerManager mappedEntryListenerManager;

	private ResourcesScanner scanner = ResourcesScanner.CLASS_CANNER;
	private String[] packagesToScan;
	
	private ApplicationContext applicationContext;
	
	private Object entryCacheLock = new Object();

	public MutilMappedEntryManager() {
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(LangUtils.isEmpty(mapedEntryBuilders)){
			this.mapedEntryBuilders = SpringUtils.getBeans(applicationContext, MappedEntryBuilder.class);
		}
		Assert.notEmpty(mapedEntryBuilders, "no mapped entry builders ...");
		
		List<MappedEntryManagerListener> mappedEntryBuilderEvents = SpringUtils.getBeans(applicationContext, MappedEntryManagerListener.class);
		Assert.notEmpty(mappedEntryBuilderEvents, "no mapped entry events ...");
		this.mappedEntryListenerManager = new MappedEntryListenerManager(this);
		this.mappedEntryListenerManager.setMappedEntryBuilderEvents(mappedEntryBuilderEvents);

		if (!LangUtils.isEmpty(packagesToScan)) {
			List<ScanedClassContext> entryClassNameList = scanner.scan(new ScanResourcesCallback<ScanedClassContext>() {

				/*@Override
				public boolean isCandidate(MetadataReader metadataReader) {
					String className = metadataReader.getClassMetadata().getClassName();
					Class<?> clazz = ReflectUtils.loadClass(className);
					return isSupported(clazz);
					return isSupported(metadataReader);
				}*/

				@Override
				public ScanedClassContext doWithCandidate(MetadataReader metadataReader, Resource resource, int count) {
					/*Class<?> clazz = mappedEntryListenerManager.fireBeforeBuildMappedEntryEvents(metadataReader);
					if(clazz==null){
						clazz = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName());
					}*/
					if(!isSupported(metadataReader))
						return null;
					return new ScanedClassContext(metadataReader);
				}

			}, packagesToScan);
			

			mappedEntryListenerManager.fireBeforeBuildEvents(entryClassNameList);
			
			JFishList<JFishMappedEntry> entryList = JFishList.create();
			int count = 0;
			for(ScanedClassContext ctx : entryClassNameList){
				String clsName = ctx.getClassName();
				Class<?> clazz = ReflectUtils.loadClass(clsName);
				
				JFishMappedEntry entry = buildMappedEntry(clazz);
				if(entry==null)
					throw new JFishOrmException("can not build the entity : " + clazz);
				buildEntry(entry);
				logger.info("build entity entry[" + (count++) + "]: " + entry.getEntityName());
				entryList.add(entry);
				
				String key = getCacheKey(entry.getEntityClass());
				if(StringUtils.isNotBlank(key))
					putInCache(key, entry);
				
				mappedEntryListenerManager.fireAfterBuildEvents(entry);
			}

			mappedEntryListenerManager.fireAfterAllEntriesHaveBuiltEvents(entryList);
			for(JFishMappedEntry entry : entryList){
				entry.freezing();
			}
		}

	}
	protected void buildEntry(JFishMappedEntry entry){
		try {
				//TODO
				//此方法会延迟调用，会设置各种属性和manager的事件回调后，才会调用，
				//所以，如果没有实现扫描和构建所有实体，而在运行时才build，就要注意多线程的问题
			entry.buildEntry();
		} catch (Exception e) {
			throw new JFishOrmException("build entry["+entry.getEntityName()+"] error: "+e.getMessage(), e);
		}
	}
	protected JFishMappedEntry getFromCache(String key) {
		return entryCache.get(key);
	}

	protected void putInCache(String key, JFishMappedEntry entry) {
		this.entryCache.put(key, entry);
	}

	@Override
	public boolean isSupported(Object entity) {
		for (MappedEntryBuilder em : mapedEntryBuilders) {
			if (em.isSupported(entity))
				return true;
		}
		return false;
	}

	@Override
	public JFishMappedEntry findEntry(Object object) {
		try {
			return getEntry(object);
		} catch (JFishNoMappedEntryException e) {
			//ignore
		} catch (JFishOrmException e) {
			throw e;
		} catch (Exception e) {
			throw new JFishOrmException("find entry error: " + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public JFishMappedEntry getEntry(Object objects) {
		Assert.notNull(objects, "the object arg can not be null!");
		JFishMappedEntry entry = null;
		
		Object object = LangUtils.getFirst(objects);
		if(object==null)
			throw new JFishNoMappedEntryException("object can not null or emtpty, objects:"+objects);
		
		if(String.class.isInstance(object) && object.toString().indexOf('.')!=-1){
			object = ReflectUtils.loadClass(object.toString());
		}
		
		String key = getCacheKey(object);
		
		if (StringUtils.isBlank(key)) {
			entry = buildMappedEntry(object);
//			mappedEntryListenerManager.fireAfterBuildEvents(entry);
			buildEntry(entry);
			mappedEntryListenerManager.fireAfterBuildEvents(entry);
			entry.freezing();
			return entry;
		}
		
		//多判断一次，如果是预先加载了，就不用进入同步块
		if(entryCache.containsKey(key))
			return entryCache.get(key);

		// syn?
		synchronized (entryCacheLock) {
			entry = getFromCache(key);
			if (entry != null) {
				return entry;
			}

			entry = buildMappedEntry(object);

			if (entry == null)
				throw new JFishNoMappedEntryException("can find build entry for this object, may be no mapping : " + object.getClass());

//			mappedEntryListenerManager.fireAfterBuildEvents(entry);
			buildEntry(entry);
			
			putInCache(key, entry);
			//build after put it into cache
			mappedEntryListenerManager.fireAfterBuildEvents(entry);
			entry.freezing();
		}
		
		logger.info("put entry into cache : " + key);
		return entry;
	}

	@Override
	public JFishMappedEntry buildMappedEntry(Object object) {
		JFishMappedEntry entry = null;
		for (MappedEntryBuilder em : mapedEntryBuilders) {
			if (!em.isSupported(object))
				continue;
			entry = em.buildMappedEntry(object);
			if (entry != null){
				return entry;
			}
		}
		throw new JFishNoMappedEntryException("jfish orm unsupported the type["+ReflectUtils.getObjectClass(object)+"] as a entity");
//		return entry;
	}


	public String getCacheKey(Object object) {
		String key = null;
		if (Map.class.isInstance(object)) {

		} else {
			Class<?> entityClass = ReflectUtils.getObjectClass(LangUtils.getFirst(object));
			key = entityClass.getName();
		}
		return key;
	}

	public void setMappedEntryBuilder(List<MappedEntryBuilder> managers) {
		this.mapedEntryBuilders = managers;
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
