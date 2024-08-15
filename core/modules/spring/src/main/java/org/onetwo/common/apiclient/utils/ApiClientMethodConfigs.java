package org.onetwo.common.apiclient.utils;

import org.onetwo.common.apiclient.ApiClientMethodConfig;
import org.onetwo.common.apiclient.impl.DynamicApiClientMethodConfig;

public class ApiClientMethodConfigs {
	
	public static final ApiClientMethodConfig DISABLED_THROW_IF_ERROR = new DynamicApiClientMethodConfig(false);

}
