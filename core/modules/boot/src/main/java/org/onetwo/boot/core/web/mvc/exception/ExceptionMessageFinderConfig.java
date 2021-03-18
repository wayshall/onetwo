package org.onetwo.boot.core.web.mvc.exception;

import java.util.Collections;
import java.util.Map;

import org.onetwo.boot.utils.BootUtils;

/**
 * @author weishao zeng
 * <br/>
 */

public interface ExceptionMessageFinderConfig {
	String OTHER_MAPPING_KEY = "*";
	/***
	 * 是否一只显示详细log
	 * @author weishao zeng
	 * @return
	 */
	boolean isAlwaysLogErrorDetail();
	
	Map<String, Integer> getExceptionsStatusMapping();
	Map<String, ExceptionMapping> getExceptionsMapping();

	default boolean isInternalError(Exception ex){
		if(BootUtils.isHystrixErrorPresent()){
			String name = ex.getClass().getName();
			return name.endsWith("HystrixRuntimeException") || 
					name.endsWith("HystrixBadRequestException") || 
					name.equals("feign.codec.DecodeException");
		}
		return false;
	}
	
	ExceptionMessageFinderConfig DEFAULT = new ExceptionMessageFinderConfig() {
		public boolean isAlwaysLogErrorDetail() {
			return false;
		}
		public Map<String, Integer> getExceptionsStatusMapping() {
			return Collections.emptyMap();
		}
		public Map<String, ExceptionMapping> getExceptionsMapping() {
			return Collections.emptyMap();
		}
	};

}
