package org.onetwo.common.jackson.serializer;

import java.io.IOException;

import org.onetwo.common.jackson.JsonMapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author weishao zeng
 * <br/>
 */
public class TypingJsonDataDeserializer extends JsonDeserializer<Object> {
	private JsonMapper jsonMapper = JsonMapper.ignoreEmpty().enableTyping();

	@Override
	public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String val = p.getText();
		return jsonMapper.fromJson(val, Object.class);
	} 


}

