package org.onetwo.boot.module.swagger.util;

import org.onetwo.common.jackson.JsonMapper;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
public class SwaggerUtils {
	private static final String REF_PREFIX = "#/definitions/";
	
	private static final JsonMapper JSON_MAPPER = JsonMapper.ignoreEmpty().enableTyping();
	
	
	public static JsonMapper getJsonMapper() {
		return JSON_MAPPER;
	}

	public static String getModelRefPath(String name){
		Assert.hasText(name, "name must has text");
		return REF_PREFIX + name;
	}
	
	public static String toJson(Object obj){
		return JSON_MAPPER.toJson(obj);
	}
	
	private SwaggerUtils(){
	}

}
