package org.onetwo.dbm.ui.json;

import org.onetwo.boot.core.json.ObjectMapperProvider.ObjectMapperCustomizer;
import org.onetwo.dbm.ui.json.ObjectNodeInputFieldSerializer.ObjectNodeMixin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author weishao zeng
 * <br/>
 */
public class DUIObjectMapperCustomizer implements ObjectMapperCustomizer {

	@Override
	public void afterCreate(ObjectMapper objectMapper) {
		objectMapper.addMixIn(ObjectNode.class, ObjectNodeMixin.class);
	}
	

}
