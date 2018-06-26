package org.onetwo.common.apiclient.utils;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.util.ClassUtils;

/**
 * @author wayshall
 * <br/>
 */
abstract public class ApiClientUtils {
	
	private static final Logger apiclientLogger = JFishLoggerFactory.getLogger("JFishRestApiClient");
	
	public static final String CLASS_REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping";
	
	public static boolean isRequestMappingPresent(){
		return ClassUtils.isPresent(CLASS_REQUEST_MAPPING, null);
	}

	public static Logger getApiclientlogger() {
		return apiclientLogger;
	}

}
