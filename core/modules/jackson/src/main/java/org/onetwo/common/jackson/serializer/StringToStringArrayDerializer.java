package org.onetwo.common.jackson.serializer;

import java.io.IOException;

import org.onetwo.common.utils.GuavaUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author weishao zeng
 * <br/>
 */
public class StringToStringArrayDerializer extends JsonDeserializer<String[]> {
	
	private String splitor = ",";
	

	public StringToStringArrayDerializer() {
		super();
	}

	public StringToStringArrayDerializer(String splitor) {
		super();
		this.splitor = splitor;
	}

	@Override
	public String[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String val = p.getText();
		return GuavaUtils.split(val, splitor);
	}

}

