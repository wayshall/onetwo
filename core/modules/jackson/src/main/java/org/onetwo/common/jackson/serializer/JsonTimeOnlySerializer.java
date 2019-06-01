package org.onetwo.common.jackson.serializer;

import java.io.IOException;
import java.util.Date;

import org.onetwo.common.date.DateUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonTimeOnlySerializer extends JsonSerializer<Date>{

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		String val = DateUtils.formatDateByPattern(value, DateUtils.TIME_ONLY);
		jgen.writeString(val);
	}
	
}
