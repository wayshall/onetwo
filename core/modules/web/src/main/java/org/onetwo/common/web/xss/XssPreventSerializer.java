package org.onetwo.common.web.xss;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class XssPreventSerializer extends JsonSerializer<String>{

	@Override
	public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		String newValue = XssUtils.escapeIfNeccessary(value);
		jgen.writeString(newValue);
	}
	
}
