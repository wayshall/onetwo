package org.onetwo.cloud.hystrix;

import java.util.concurrent.Callable;

import org.onetwo.cloud.env.AuthEnvs;
import org.onetwo.cloud.env.AuthEnvs.AuthEnv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 可考虑用HystrixRequestContext代替实现变量共享
 * 详见：HystrixContexSchedulerAction
 * 
 * @author wayshall
 * <br/>
 */
public class RequestContextConcurrencyStrategy extends AbstractContextConcurrencyStrategy {

	@Autowired
	private AuthEnvs authEnvs;
	
	@Override
	protected <V> Callable<V> createDelegatingContextCallable(Callable<V> callable) {
		return new RequestContextCallable<>(callable);
	}
	
	class RequestContextCallable<T> implements Callable<T> {
		private Callable<T> delegate;
//		private final RequestAttributes delegateRequestAttributes;
//		private String authorizationToken;
		private AuthEnv authEnv;
		
		public RequestContextCallable(Callable<T> callable) {
			super();
			this.delegate = callable;
//			this.delegateRequestAttributes = RequestContextHolder.getRequestAttributes();
			/*OAuth2Utils.getCurrentToken().ifPresent(token -> {
				this.authorizationToken = token;
			});*/
//			this.authEnv = AuthEnvs.getCurrent();
			AuthEnv env = AuthEnvs.getCurrent();
			if (env==null) {
				env = authEnvs.createWebAuthEnv(false);
			}
			this.authEnv = env;
		}

		@Override
		public T call() throws Exception {
			RequestAttributes existRequestAttributes = RequestContextHolder.getRequestAttributes();
			//当前线程的attrs和代理的不一致，则需要重置
			boolean needToSetContext = authEnv!=null && authEnv.getRequestAttributes()!=null && !authEnv.getRequestAttributes().equals(existRequestAttributes);
//			boolean hasOauth2Ctx = StringUtils.isNotBlank(authorizationToken);
			try {
				/*RequestContextHolder.setRequestAttributes(delegateRequestAttributes);
				if (hasOauth2Ctx) {
					OAuth2Utils.setCurrentToken(authorizationToken);
				}
				AuthEnvs.setCurrent(authEnv);*/
				if (authEnv!=null) {
					if (needToSetContext) {
						RequestContextHolder.setRequestAttributes(authEnv.getRequestAttributes());
					}
					AuthEnvs.setCurrent(authEnv);
				}
				return this.delegate.call();
			} finally {
				/*if (hasOauth2Ctx) {
					OAuth2Utils.removeCurrentToken();
				}
				*/
				if (authEnv!=null) {
					AuthEnvs.removeCurrent();
				}
				if (needToSetContext){
					RequestContextHolder.setRequestAttributes(existRequestAttributes);
				}
			}
		}
		
	}

}
