package org.onetwo.common.apiclient.interceptor;

import java.io.IOException;

import org.onetwo.common.apiclient.annotation.RestExecutorInterceptor;
import org.onetwo.common.apiclient.utils.ApiClientUtils;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * @author wayshall
 * <br/>
 */
@RestExecutorInterceptor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExecutorSimpleLogInterceptor implements ClientHttpRequestInterceptor {
	private Logger logger = ApiClientUtils.getApiclientlogger();

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		if(logger.isDebugEnabled()){
			logger.debug("RestExecutor url: {}", request.getURI());
		}
		return execution.execute(request, body);
	}

}
