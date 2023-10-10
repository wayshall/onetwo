package org.onetwo.common.apiclient;

public interface ApiClientMethodConfig {
	
	default boolean isThrowIfError() {
		return true;
	};
	
}
