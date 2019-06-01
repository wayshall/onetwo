package org.onetwo.common.jackson.serializer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.onetwo.common.utils.GuavaUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author weishao zeng
 * <br/>
 */
public class StringToListDerializer extends JsonDeserializer<List<String>> {
	
	private String splitor = ",";
	

	public StringToListDerializer() {
		super();
	}

	public StringToListDerializer(String splitor) {
		super();
		this.splitor = splitor;
	}

	@Override
	public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String val = p.getText();
		return GuavaUtils.splitAsStream(val, splitor).collect(Collectors.toList());
	}
	

	
	public static class VerticalSplitorToListDerializer extends StringToListDerializer {

		public VerticalSplitorToListDerializer() {
			super("|");
		}
		
	}

}

