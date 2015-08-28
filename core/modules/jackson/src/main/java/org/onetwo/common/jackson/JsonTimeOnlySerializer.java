package org.onetwo.common.jackson;

import java.io.IOException;
import java.util.Date;

import org.onetwo.common.date.DateUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonTimeOnlySerializer extends JsonSerializer<Date>{

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		String val = DateUtil.formatDateByPattern(value, DateUtil.Time_Only);
		jgen.writeString(val);
	}
	
}
