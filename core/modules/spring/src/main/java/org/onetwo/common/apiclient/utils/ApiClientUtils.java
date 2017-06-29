package org.onetwo.common.apiclient.utils;

import org.springframework.util.ClassUtils;

/**
 * @author wayshall
 * <br/>
 */
abstract public class ApiClientUtils {
	
	public static final String CLASS_REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping";
	
	public static boolean isRequestMappingPresent(){
		return ClassUtils.isPresent(CLASS_REQUEST_MAPPING, null);
	}

}
