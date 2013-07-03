package org.onetwo.common.jackson;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.onetwo.common.utils.DateUtil;

public class JsonDateOnlySerializer extends JsonSerializer<Date>{

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		String val = DateUtil.formatDateByPattern(value, DateUtil.Date_Only);
		jgen.writeString(val);
	}
	
}
