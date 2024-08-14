package org.onetwo.common.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.onetwo.common.exception.BaseException;

/**
 * @author weishao zeng
 * <br/>
 */
abstract public class FutureUtils {
	
	public static <T> T get(Future<T> future) {
		try {
			return future.get();
		} catch (InterruptedException e) {
			throw new BaseException("async task has interrupted: " + e.getMessage(), e);
		} catch (ExecutionException e) {
			throw new BaseException("async task execute error: " + e.getMessage(), e);
		}
	}
	
	private FutureUtils() {
	}

}
