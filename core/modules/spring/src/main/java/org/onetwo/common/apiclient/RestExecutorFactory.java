package org.onetwo.common.apiclient;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author wayshall
 * <br/>
 */
abstract public class RestExecutorFactory implements FactoryBean<RestExecutor>{
	public static final String REST_EXECUTOR_FACTORY_BEAN_NAME = "apiClientRestExecutor";
	
	protected RestExecutor restExecutor = null;

	abstract protected RestExecutor createRestExecutor();

	@Override
	public RestExecutor getObject() throws Exception {
		if(restExecutor==null){
			restExecutor = createRestExecutor();
		}
		return restExecutor;
	}

	@Override
	public Class<?> getObjectType() {
		return RestExecutor.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	
}
