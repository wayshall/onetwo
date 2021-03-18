package org.onetwo.boot.core.web.async;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.Springs;

/**
 * @author wayshall
 * <br/>
 */
public class Asyncs {

	private static AsyncTaskDelegateService delegateService;
	private static boolean hasInited = false;
	private static Object lockObj = new Object();
	
	/***
	 * 若能找到异步服务，则尝试异步执行
	 * @author weishao zeng
	 * @param runnable
	 */
	public static void tryAsyncRun(Runnable runnable){
		Optional<AsyncTaskDelegateService> delegateOpt = delegateOpt();
		if (delegateOpt.isPresent()) {
			delegateOpt.get().run(runnable);
		} else {
			runnable.run();
		}
	}
	
	public static void run(Runnable runnable){
		delegate().run(runnable);
	}
	
	public static <T> CompletableFuture<T> await(Supplier<T> supplier){
		return delegate().await(supplier);
	}
	
	public static AsyncTaskDelegateService delegate(){
		return delegateOpt().orElseThrow(() -> new BaseException("AsyncTaskDelegateService not found!"));
	}
	
	public static Optional<AsyncTaskDelegateService> delegateOpt(){
		AsyncTaskDelegateService ds = delegateService;
		if(ds==null){
			synchronized (lockObj) {
				if (!hasInited) {
					ds = Springs.getInstance().getBean(AsyncTaskDelegateService.class);
					delegateService = ds;
					hasInited = true;
				} else {
					ds = delegateService;
				}
			}
		}
		return Optional.ofNullable(ds);
	}
	
	private Asyncs(){
	}

}
