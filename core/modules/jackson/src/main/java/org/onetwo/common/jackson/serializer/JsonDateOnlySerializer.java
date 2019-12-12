package org.onetwo.common.jackson.serializer;

import java.io.IOException;
import java.util.Date;

import org.onetwo.common.date.DateUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class JsonDateOnlySerializer extends JsonSerializer<Date>{

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		String val = DateUtils.formatDateByPattern(value, DateUtils.DATE_ONLY);
		jgen.writeString(val);
	}


	@Override
	public void serializeWithType(Date value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
		typeSer.writeTypePrefixForScalar(value, gen);
//		typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
		serialize(value, gen, serializers);
//		typeSer.writeTypeSuffix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
		typeSer.writeTypeSuffixForScalar(value, gen);
	}
}
