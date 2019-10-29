package org.onetwo.common.jackson.serializer;

import java.io.IOException;

import org.onetwo.common.jackson.JsonMapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author weishao zeng
 * <br/>
 */
public class TypingJsonDataSerializer extends JsonSerializer<Object> {
	private JsonMapper jsonMapper = JsonMapper.defaultMapper().enableTyping(); 

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		String json = jsonMapper.toJson(value);
		gen.writeString(json);
	}

}

