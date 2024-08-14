package org.onetwo.common.spring.converter;

import org.onetwo.common.jackson.JsonMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author weishao zeng
 * <br/>
 */
public class StringToJackson2JsonNodeConverterFactory implements ConverterFactory<String, JsonNode> {

	@Override
	public <R extends JsonNode> Converter<String, R> getConverter(Class<R> targetType) {
		return new StringToJsonNode<>(targetType);
	}

	private class StringToJsonNode<T extends JsonNode> implements Converter<String, T> {

		private JsonMapper jsonMapper = JsonMapper.IGNORE_NULL;
		private Class<T> targetType;
		
		public StringToJsonNode(Class<T> targetType) {
			this.targetType = targetType;
		}

		@Override
		public T convert(String source) {
			T node = jsonMapper.fromJson(source, targetType);
			return node;
		}
		
	}

}
