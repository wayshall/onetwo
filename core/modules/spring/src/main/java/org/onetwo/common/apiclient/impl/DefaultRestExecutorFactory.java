package org.onetwo.common.apiclient.impl;

import org.onetwo.common.apiclient.RestExecutor;
import org.onetwo.common.apiclient.RestExecutorFactory;
import org.onetwo.common.spring.rest.ExtRestTemplate;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultRestExecutorFactory implements RestExecutorFactory {

	@Override
	public RestExecutor createRestExecutor() {
		ExtRestTemplate restTemplate = new ExtRestTemplate();
		return restTemplate;
	}
	
	

}
