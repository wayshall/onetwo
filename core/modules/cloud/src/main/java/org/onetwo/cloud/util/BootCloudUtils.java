package org.onetwo.cloud.util;

import org.springframework.util.ClassUtils;

/**
 * @author wayshall
 * <br/>
 */
public class BootCloudUtils {
	
	private static final boolean netflixFeignClientPresent = ClassUtils.isPresent("org.springframework.cloud.netflix.feign.FeignClient", ClassUtils.getDefaultClassLoader());

	public static boolean isNetflixFeignClientPresent(){
		return netflixFeignClientPresent;
	}
	
	private BootCloudUtils(){
	}
}
