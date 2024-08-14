package org.onetwo.cloud.util;

import org.springframework.util.ClassUtils;

/**
 * @author wayshall
 * <br/>
 */
public class BootCloudUtils {
	
	public static final String FEIGN_CLIENT_CLASS_NAME = "org.springframework.cloud.openfeign.FeignClient";
	public static final String FEIGN_CLASS_NAME = "feign.Feign";
	
	private static final boolean netflixFeignClientPresent = ClassUtils.isPresent(FEIGN_CLIENT_CLASS_NAME, ClassUtils.getDefaultClassLoader());

	public static boolean isNetflixFeignClientPresent(){
		return netflixFeignClientPresent;
	}
	
	private BootCloudUtils(){
	}
}
