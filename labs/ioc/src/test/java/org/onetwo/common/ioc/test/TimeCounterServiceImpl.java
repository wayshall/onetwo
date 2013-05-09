package org.onetwo.common.ioc.test;

import java.util.Date;

import org.onetwo.common.cache.MethodCacheBFInterceptor;
import org.onetwo.common.cache.annotation.Cacheable;
import org.onetwo.common.ioc.annotation.BFComponent;
import org.onetwo.common.ioc.annotation.BFInterceptors;
import org.onetwo.common.utils.timeit.Timeit;

@BFInterceptors({MethodCacheBFInterceptor.class})
@BFComponent(businessInterfaces=TimeCounterService.class)
public class TimeCounterServiceImpl implements TimeCounterService {
	
	public TimeCounterServiceImpl(){
	}
	
	@Cacheable
	@Timeit
	public String getTimeMethod(int second){
		try {
			Thread.sleep(second*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Date().toLocaleString();
	}

}
