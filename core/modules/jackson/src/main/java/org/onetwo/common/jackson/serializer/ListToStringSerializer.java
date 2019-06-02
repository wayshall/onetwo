package org.onetwo.common.jackson.serializer;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

/**
 * 把list<String> 写成 value1,value2,value3
 * 与 StringToListDerializer 对应
 * @author weishao zeng
 * <br/>
 */
public class ListToStringSerializer extends JsonSerializer<List<?>> {
	
	private String joiner = ",";
	
	public ListToStringSerializer() {
	}

	public ListToStringSerializer(String joiner) {
		super();
		this.joiner = joiner;
	}

	@Override
	public void serialize(List<?> value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		String val = StringUtils.join(value, joiner);
		gen.writeString(val);
	}
	

	@Override
	public void serializeWithType(List<?> value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
//		typeSer.writeTypePrefixForScalar(value, gen);
//		typeSer.writeTypePrefixForArray(value, gen);
		serialize(value, gen, serializers);
//		typeSer.writeTypeSuffixForScalar(value, gen);
//		typeSer.writeTypeSuffixForArray(value, gen);
	}
	
	public static class ListToVerticalJoinerStringSerializer extends ListToStringSerializer {

		public ListToVerticalJoinerStringSerializer() {
			super("|");
		}
		
	}

}

