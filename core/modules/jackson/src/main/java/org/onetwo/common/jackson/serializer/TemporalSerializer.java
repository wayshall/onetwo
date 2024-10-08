package org.onetwo.common.jackson.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;

import org.onetwo.common.date.Dates;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

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

	@Override
	public void serializeWithType(Temporal value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
//		typeSer.writeTypePrefixForScalar(value, gen);
		typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
		serialize(value, gen, serializers);
		typeSer.writeTypeSuffix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
//		typeSer.writeTypeSuffixForScalar(value, gen);
	}
	
}
