package org.onetwo.common.jackson.serializer;

import java.io.IOException;
import java.util.Date;

import org.onetwo.common.date.DateUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonCommonDateDerializer extends JsonDeserializer<Date>{

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt)throws IOException, JsonProcessingException {
		String val = p.getText();
		Date date = DateUtils.parse(val);
		return date;
	}

}
