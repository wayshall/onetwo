package org.onetwo.common.apiclient.impl;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class RestExecutorConfig {
	int connectTimeout = 60_000;
	int readTimeout = 60_000;
	int writeTimeout = 60_000;
}
