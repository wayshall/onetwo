package org.onetwo.boot.module.cache;

import org.onetwo.common.db.spi.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;

/**
 * 
 * @author wayshall
 * <br/>
 */
@Service
@Transactional
public class UserCacheTestService {
	
	@Autowired
	BaseEntityManager baseEntityManager;
	
	/**
	 * cacheType为REMOTE或者BOTH的时候，刷新行为是全局唯一的，也就是说，即使应用服务器是一个集群，也不会出现多个服务器同时去刷新一个key的情况。
	 * 
	 * 一个key的刷新任务，自该key首次被访问后初始化，如果该key长时间不被访问，
	 * 在stopRefreshAfterLastAccess指定的时间后，相关的刷新任务就会被自动移除，这样就避免了浪费资源去进行没有意义的刷新。
	 */
	@Cached(name="userCache:", cacheType=CacheType.REMOTE, key="#userId", expire=100, cacheNullValue=true)
	@CacheRefresh(refresh=3, stopRefreshAfterLastAccess=100)
	public UserEntity findByIdWithCache(Long userId){
		return baseEntityManager.findById(UserEntity.class, userId);
	}

	public UserEntity save(UserEntity user){
		baseEntityManager.save(user);
		return user;
	}
	
	@CacheUpdate(name="userCache:", key="#user.id", value="#user")
	public UserEntity updateWithCache(UserEntity user){
		baseEntityManager.update(user);
		return user;
	}
	
	@CacheInvalidate(name="userCache:", key="#userId")
	public void clearCache(Long userId){
	}

}
