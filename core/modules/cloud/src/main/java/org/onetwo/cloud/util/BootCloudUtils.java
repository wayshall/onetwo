package org.onetwo.cloud.util;

import org.springframework.util.ClassUtils;

/**
 * @author wayshall
 * <br/>
 */
public class BootCloudUtils {

	public static boolean isNetflixFeignClientPresent(){
		return ClassUtils.isPresent("org.springframework.cloud.netflix.feign.FeignClient", ClassUtils.getDefaultClassLoader());
	}
	
	private BootCloudUtils(){
	}
}
