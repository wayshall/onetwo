package org.onetwo.common.ioc.test;

import org.onetwo.common.cache.MethodCacheBFInterceptor;
import org.onetwo.common.cache.annotation.Cacheable;
import org.onetwo.common.ioc.annotation.BFComponent;
import org.onetwo.common.ioc.annotation.BFInterceptors;

@BFInterceptors({MethodCacheBFInterceptor.class})
@BFComponent(businessInterfaces=CacheService.class)
public class CacheServiceImpl implements CacheService {
	
	public static int count = 0;
	
	
	public CacheServiceImpl(){
		System.out.println("asdf");
	}

	@Cacheable(expire=CacheService.expireTime)
	public String getString(String data){
		return data+"--not--cache-result--"+(count++);
	}
	

}
