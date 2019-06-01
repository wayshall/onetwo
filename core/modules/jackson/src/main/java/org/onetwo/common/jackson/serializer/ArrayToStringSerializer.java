package org.onetwo.common.jackson.serializer;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author weishao zeng
 * <br/>
 */
public class ArrayToStringSerializer extends JsonSerializer<Object[]> {
	
	private String joiner = ",";
	
	public ArrayToStringSerializer() {
	}

	public ArrayToStringSerializer(String joiner) {
		super();
		this.joiner = joiner;
	}

	@Override
	public void serialize(Object[] value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		String val = StringUtils.join(value, joiner);
		gen.writeString(val);
	}

}

