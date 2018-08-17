package org.onetwo.boot.plugins.swagger.util;

import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
public class SwaggerUtils {
	private static final String REF_PREFIX = "#/definitions/";
	public static final String API_ID_PREFIX = "api";
	private static final String EXTENSION_PREFIX = "x-field-";
	
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
	

    public static void setExtendProperties(Object entity, Map<String, Object> vendorExtensions){
    	if(LangUtils.isEmpty(vendorExtensions)){
    		return ;
    	}
    	BeanWrapper bw = SpringUtils.newBeanWrapper(entity);
    	for(Entry<String, Object> entry: vendorExtensions.entrySet()){
    		String propName = entry.getKey().substring(EXTENSION_PREFIX.length());
    		propName = StringUtils.toCamel(propName, '-', false);
    		bw.setPropertyValue(propName, entry.getValue());
    	}
    }
	
	private SwaggerUtils(){
	}

}
