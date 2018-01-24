package org.onetwo.boot.core.web.async;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.Springs;

/**
 * @author wayshall
 * <br/>
 */
public class Asyncs {

	volatile private static AsyncTaskDelegateService delegateService;
	
	public static void run(Runnable runnable){
		delegate().run(runnable);
	}
	
	public static <T> CompletableFuture<T> await(Supplier<T> supplier){
		return delegate().await(supplier);
	}
	
	public static AsyncTaskDelegateService delegate(){
		AsyncTaskDelegateService ds = delegateService;
		if(ds==null){
			ds = Springs.getInstance().getBean(AsyncTaskDelegateService.class);
			if(ds==null){
				throw new BaseException("no AsyncTaskDelegateService found! Maybe ["+AsyncTaskProperties.ENABLE_KEY+"] not enabled!");
			}
			delegateService = ds;
		}
		return ds;
	}
	
	private Asyncs(){
	}

}
