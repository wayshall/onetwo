package org.onetwo.boot.core.web.async;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Async;

/**
 * @author wayshall
 * <br/>
 */
public class AsyncTaskDelegateService {
	
	@Async
	public void run(Runnable runnable){
		runnable.run();
	}
	
	@Async
	public <T> CompletableFuture<T> await(Supplier<T> supplier){
		T result = supplier.get();
		return CompletableFuture.completedFuture(result);
	}

}
