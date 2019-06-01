package org.onetwo.common.jackson.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;

import org.onetwo.common.date.Dates;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TemporalSerializer extends JsonSerializer<Temporal>{

	@Override
	public void serialize(Temporal value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		if(value instanceof LocalDate){
			String val = Dates.formatDate((LocalDate)value);
			jgen.writeString(val);
		}else if(value instanceof LocalDateTime){
			LocalDateTime dt = (LocalDateTime) value;
			String val = dt.format(Dates.DATE_TIME);
			jgen.writeString(val);
		}else if(value instanceof LocalTime){
			LocalTime dt = (LocalTime) value;
			String val = dt.format(Dates.TIME_ONLY);
			jgen.writeString(val);
		}else{
			jgen.writeObject(value);
		}
	}
	
}
