package org.onetwo.common.jackson.deserializer;

import java.io.IOException;

import org.onetwo.common.convert.Types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ToBooleanDeserializer extends JsonDeserializer<Boolean> {

	@Override
	public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		boolean res = Types.asBoolean(p.getText());
		return res;
	}

}
