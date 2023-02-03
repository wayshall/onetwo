package org.onetwo.common.apiclient;

import org.springframework.http.client.ClientHttpRequest;

/**
 * @author weishao zeng
 * <br/>
 */
public interface ApiRequestCallback {
	
	void doWithRequest(ClientHttpRequest request);
}
