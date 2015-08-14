package org.onetwo.common.jfishdbm.mapping;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.onetwo.common.jfishdbm.dialet.DBDialect;
import org.onetwo.common.jfishdbm.exception.DBException;
import org.onetwo.common.jfishdbm.exception.JFishNoMappedEntryException;
import org.onetwo.common.jfishdbm.exception.JFishOrmException;
import org.onetwo.common.jfishdbm.jpa.JPAMappedEntryBuilder;
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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;

public class MutilMappedEntryManager implements MappedEntryBuilder, MappedEntryManager {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

//	private Map<String, JFishMappedEntry> entryCache = new ConcurrentHashMap<String, JFishMappedEntry>();
	private List<MappedEntryBuilder> mappedEntryBuilders;
//	private MappedEntryListenerManager mappedEntryListenerManager;

	private ResourcesScanner scanner = ResourcesScanner.CLASS_CANNER;
//	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private DBDialect dialet;
	
	private Cache<String, JFishMappedEntry> entryCaches = CacheBuilder.newBuilder().build();

	public MutilMappedEntryManager(DBDialect dialet) {
		this.dialet = dialet;
	}


	/***
	 * first
	 */
	@Override
	public void initialize() {
		if(LangUtils.isEmpty(mappedEntryBuilders)){
			List<MappedEntryBuilder> builders = LangUtils.newArrayList();
			MappedEntryBuilder builder = new JFishMappedEntryBuilder(dialet);
			builder.initialize();
			builders.add(builder);
			
			builder = new JPAMappedEntryBuilder(dialet);
			builder.initialize();
			builders.add(builder);
			this.mappedEntryBuilders = ImmutableList.copyOf(builders);
		}
	}


	/***
	 * second
	 */
	@Override
	public void scanPackages(String... packagesToScan) {
		Assert.notEmpty(mappedEntryBuilders, "no mapped entry builders ...");
		
		if (!LangUtils.isEmpty(packagesToScan)) {
			List<ScanedClassContext> entryClassNameList = scanner.scan(new ScanResourcesCallback<ScanedClassContext>() {

				@Override
				public ScanedClassContext doWithCandidate(MetadataReader metadataReader, Resource resource, int count) {
					if(!isSupported(metadataReader))
						return null;
					return new ScanedClassContext(metadataReader);
				}

			}, packagesToScan);
			

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
				
			}

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

	private void putInCache(String key, JFishMappedEntry entry) {
		this.entryCaches.put(key, entry);
		logger.info("put entry into cache : " + key);
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
		
		try {
			entry = entryCaches.getIfPresent(key);
			if(entry==null){
				final Object entityObject = object;
				entry = entryCaches.get(key, ()->{
					JFishMappedEntry value = buildMappedEntry(entityObject);
	
					if (value == null)
						throw new JFishNoMappedEntryException("can find build entry for this object, may be no mapping : " + entityObject.getClass());
	
					buildEntry(value);
					putInCache(key, value);
					value.freezing();
					return value;
				});
			}
		} catch (ExecutionException e) {
			throw new DBException("create entry error for entity: " + object, e);
		}
		return entry;
		
		//多判断一次，如果是预先加载了，就不用进入同步块
		/*if(entryCache.containsKey(key))
			return entryCache.get(key);

//		synchronized (entryCacheLock) {
		lock.readLock().lock();
		try {
			if(!entryCache.containsKey(key)){
//				return entryCache.get(key);
				lock.readLock().unlock();
				lock.writeLock().lock();
				try {
					entry = buildMappedEntry(object);

					if (entry == null)
						throw new JFishNoMappedEntryException("can find build entry for this object, may be no mapping : " + object.getClass());

					buildEntry(entry);
					putInCache(key, entry);
					entry.freezing();
				} finally{
					lock.readLock().lock();//降级锁
					lock.writeLock().unlock();
				}
			}
			return entryCache.get(key);
		}finally{
			lock.readLock().unlock();
		}*/
		
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
			//no cache
		} else {
			Class<?> entityClass = ReflectUtils.getObjectClass(LangUtils.getFirst(object));
			key = entityClass.getName();
		}
		return key;
	}

	public void setMappedEntryBuilder(List<MappedEntryBuilder> managers) {
		this.mappedEntryBuilders = managers;
	}

}
