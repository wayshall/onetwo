package org.onetwo.common.jackson.serializer;

import java.io.IOException;
import java.util.stream.Collectors;

import org.onetwo.common.utils.GuavaUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author weishao zeng
 * <br/>
 */
public class StringToLongArrayDerializer extends JsonDeserializer<Long[]> {

	@Override
	public Long[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String val = p.getText();
		return GuavaUtils.splitAsStream(val, ",")
						.map(n -> Long.parseLong(n))
						.collect(Collectors.toList())
						.toArray(new Long[0]);
	}
	
	

}

