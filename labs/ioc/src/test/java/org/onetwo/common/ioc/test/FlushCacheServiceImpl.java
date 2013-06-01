package org.onetwo.common.ioc.test;

import org.onetwo.common.cache.MethodFlushCacheBFInterceptor;
import org.onetwo.common.cache.annotation.FlushCache;
import org.onetwo.common.ioc.annotation.BFComponent;
import org.onetwo.common.ioc.annotation.BFInterceptors;

@BFInterceptors({MethodFlushCacheBFInterceptor.class})
@BFComponent(businessInterfaces=FlushCacheService.class)
public class FlushCacheServiceImpl implements FlushCacheService{
	
	
	public FlushCacheServiceImpl(){
		System.out.println("asdf");
	}
	
	public void setString(String data){
	}
	
	@FlushCache
	public void setGroup(String data){
	}

}
