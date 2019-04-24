package org.onetwo.common.spring.rest;

import org.onetwo.common.apiclient.RequestContextData;
import org.springframework.core.NamedThreadLocal;

/**
 * @author weishao zeng
 * <br/>
 */
abstract public class RestExecuteThreadLocal {

	private static NamedThreadLocal<RequestContextData> contextThreadLocal = new NamedThreadLocal<>("RestExecutor Context");
	
	public static RequestContextData get() {
		return contextThreadLocal.get();
	}
	
	public static void set(RequestContextData ctx) {
		contextThreadLocal.set(ctx);
	}
	
	public static void remove() {
		contextThreadLocal.remove();
	}
}

