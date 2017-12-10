package org.onetwo.boot.core.json;

import org.onetwo.common.jackson.JsonMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author wayshall
 * <br/>
 */
public interface ObjectMapperProvider {
	
	ObjectMapper createObjectMapper();
	
	ObjectMapperProvider DEFAULT = ()->JsonMapper.ignoreNull().getObjectMapper();

}
