package org.onetwo.cloud.hystrix;

import java.util.concurrent.Callable;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author wayshall
 * <br/>
 */
public class RequestContextConcurrencyStrategy extends AbstractContextConcurrencyStrategy {

	@Override
	protected <V> Callable<V> createDelegatingContextCallable(Callable<V> callable) {
		return new RequestContextCallable<>(callable);
	}
	
	class RequestContextCallable<T> implements Callable<T> {
		private Callable<T> delegate;
		private final RequestAttributes delegateRequestAttributes;
		
		public RequestContextCallable(Callable<T> callable) {
			super();
			this.delegate = callable;
			this.delegateRequestAttributes = RequestContextHolder.getRequestAttributes();
		}

		@Override
		public T call() throws Exception {
//			RequestAttributes originRequestAttributes = RequestContextHolder.getRequestAttributes();
			try {
				RequestContextHolder.setRequestAttributes(delegateRequestAttributes);
				return this.delegate.call();
			} finally {
				RequestContextHolder.resetRequestAttributes();
			}
		}
		
	}

}
