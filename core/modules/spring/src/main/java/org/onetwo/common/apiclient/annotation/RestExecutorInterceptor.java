package org.onetwo.common.apiclient.annotation;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

/**
 * @author wayshall
 * <br/>
 */
public interface RestExecutorInterceptor {

	ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException;
	
}
