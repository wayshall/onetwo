package org.onetwo.common.apiclient.impl;

import org.onetwo.common.apiclient.ApiClientMethodConfig;

import lombok.Data;

@Data
public class DynamicApiClientMethodConfig implements ApiClientMethodConfig {
	
	final private boolean throwIfError;

	public DynamicApiClientMethodConfig(boolean throwIfError) {
		super();
		this.throwIfError = throwIfError;
	}
	

}
