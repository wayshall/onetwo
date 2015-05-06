package org.onetwo.common.jfishdb.spring;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.onetwo.common.jfishdb.exception.JFishNoMappedEntryException;
import org.onetwo.common.jfishdb.exception.JFishOrmException;
import org.onetwo.common.jfishdb.jpa.JPAMappedEntryBuilder;
import org.onetwo.common.jfishdb.orm.JFishMappedEntry;
import org.onetwo.common.jfishdb.orm.JFishMappedEntryBuilder;
import org.onetwo.common.jfishdb.orm.MappedEntryBuilder;
import org.onetwo.common.jfishdb.orm.MappedEntryManager;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.JFishList;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;

import com.google.common.collect.ImmutableList;

public class MutilMappedEntryManager implements MappedEntryBuilder, MappedEntryManager {
	
//	static enum EntryBuildEvent {
//		beforeBuild,
//		afterBuilt,
//		afterAllEntryHasBuilt
//	}

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private Map<String, JFishMappedEntry> entryCache = new ConcurrentHashMap<String, JFishMappedEntry>();
	private List<MappedEntryBuilder> mappedEntryBuilders;
//	private MappedEntryListenerManager mappedEntryListenerManager;

	private ResourcesScanner scanner = ResourcesScanner.CLASS_CANNER;
//	private String[] packagesToScan;
	
//	private ApplicationContext applicationContext;
	
	private Object entryCacheLock = new Object();
	
	private JFishDaoImplementor jfishDaoImplementor;

	public MutilMappedEntryManager(JFishDaoImplementor jfishDaoImplementor) {
		this.jfishDaoImplementor = jfishDaoImplementor;
	}


	@Override
	public void initialize() {
		if(LangUtils.isEmpty(mappedEntryBuilders)){
			List<MappedEntryBuilder> builders = LangUtils.newArrayList();
			MappedEntryBuilder builder = new JFishMappedEntryBuilder(jfishDaoImplementor.getDialect());
			builder.initialize();
			builders.add(builder);
			
			builder = new JPAMappedEntryBuilder(jfishDaoImplementor.getDialect());
			builder.initialize();
			builders.add(builder);
			this.mappedEntryBuilders = ImmutableList.copyOf(builders);
		}
	}


	@Override
	public void scanPackages(String... packagesToScan) {
		Assert.notEmpty(mappedEntryBuilders, "no mapped entry builders ...");
		
		/*List<MappedEntryManagerListener> mappedEntryBuilderEvents = SpringUtils.getBeans(applicationContext, MappedEntryManagerListener.class);
		Assert.notEmpty(mappedEntryBuilderEvents, "no mapped entry events ...");
		this.mappedEntryListenerManager = new MappedEntryListenerManager(this);
		this.mappedEntryListenerManager.setMappedEntryBuilderEvents(mappedEntryBuilderEvents);*/

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
			

//			mappedEntryListenerManager.fireBeforeBuildEvents(entryClassNameList);
			
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
				
//				mappedEntryListenerManager.fireAfterBuildEvents(entry);
			}

//			mappedEntryListenerManager.fireAfterAllEntriesHaveBuiltEvents(entryList);
			//锁定
			for(JFishMappedEntry entry : entryList){
				entry.freezing();
			}
		}

	}
	private void buildEntry(JFishMappedEntry entry){
		try {
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
		for (MappedEntryBuilder em : mappedEntryBuilders) {
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
			//for map
			entry = buildMappedEntry(object);
//			mappedEntryListenerManager.fireAfterBuildEvents(entry);
			buildEntry(entry);
//			mappedEntryListenerManager.fireAfterBuildEvents(entry);
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
//			mappedEntryListenerManager.fireAfterBuildEvents(entry);
			entry.freezing();
		}
		
		logger.info("put entry into cache : " + key);
		return entry;
	}

	@Override
	public JFishMappedEntry buildMappedEntry(Object object) {
		JFishMappedEntry entry = null;
		for (MappedEntryBuilder em : mappedEntryBuilders) {
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
		this.mappedEntryBuilders = managers;
	}

	/*public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}*/

}
