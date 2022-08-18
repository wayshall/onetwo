package org.onetwo.dbm.ui.json;

import org.onetwo.boot.core.json.ObjectMapperProvider.ObjectMapperCustomizer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author weishao zeng
 * <br/>
 */
public class DUIObjectMapperCustomizer implements ObjectMapperCustomizer {

	@Override
	public void afterCreate(ObjectMapper objectMapper) {
//		objectMapper.addMixIn(ObjectNode.class, ObjectNodeMixin.class);
	}
	

}
