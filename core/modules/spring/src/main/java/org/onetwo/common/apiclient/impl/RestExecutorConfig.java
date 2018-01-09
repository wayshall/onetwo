package org.onetwo.common.apiclient.impl;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class RestExecutorConfig {
	int connectTimeout = 50_000;
	int readTimeout = 50_000;
	int writeTimeout = 50_000;
}
