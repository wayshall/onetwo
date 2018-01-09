package org.onetwo.common.apiclient;

import org.springframework.http.HttpHeaders;

/**
 * @author wayshall
 * <br/>
 */
public interface ApiHeaderCallback {
	
	void onHeader(HttpHeaders headers);

}
