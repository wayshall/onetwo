package org.onetwo.common.async;
/**
 * 参考自：https://dzone.com/articles/asynchronous-timeouts
 * @author weishao zeng
 * <br/>
 */

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class CompletableFutureHelper {
//	private static final Logger logger = JFishLoggerFactory.getLogger(CompletableFutureHelper.class); 
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
															                1,
															                new ThreadFactoryBuilder()
															                        .setDaemon(true)
															                        .setNameFormat("CompletableFutureHelper-timeout-%d")
															                        .build());
	
//	public static <T> CompletableFuture<T> timeoutAfterFuture(Duration duration) {
//		return timeoutAfterFuture(duration, null);
//	}
	
	public static <T> CompletableFuture<T> timeoutAfterFuture(Duration duration) {
		if (duration==null) {
			throw new IllegalArgumentException("duration can not be null");
		}
		
	    final CompletableFuture<T> promise = new CompletableFuture<>();
	    scheduler.schedule(() -> {
//	    	try {
//	    		if (timeoutAction!=null) {
//	    			timeoutAction.run();
//	    		}
//			} catch (Exception e) {
//				logger.error("execute timeoutAction error.", e);
//			}
	        final TimeoutException ex = new TimeoutException("CompletableFuture Timeout after " + duration);
	        return promise.completeExceptionally(ex);
	    }, duration.toMillis(), TimeUnit.MILLISECONDS);
	    
	    return promise;
	}
	
//	public static <T> CompletableFuture<T> timeoutAfter(CompletableFuture<T> taskFuture, Duration duration) {
//		return timeoutAfter(taskFuture, duration, null);
//	}
	
	public static <T> CompletableFuture<T> timeoutAfter(CompletableFuture<T> taskFuture, Duration duration) {
		if (taskFuture==null) {
			throw new IllegalArgumentException("taskFuture can not be null");
		}
	    final CompletableFuture<T> timeoutFeture = timeoutAfterFuture(duration);
	    // taskFuture 和 timeoutFeture 任意一个完成时，执行Function.identity()，即nothing……
	    return taskFuture.applyToEither(timeoutFeture, Function.identity());
	}
}
