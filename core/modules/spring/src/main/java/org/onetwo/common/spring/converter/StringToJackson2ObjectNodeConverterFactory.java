package org.onetwo.common.spring.converter;

import org.onetwo.common.jackson.JsonMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author weishao zeng
 * <br/>
 */
public class StringToJackson2ObjectNodeConverterFactory implements ConverterFactory<String, ObjectNode> {

	@Override
	public <R extends ObjectNode> Converter<String, R> getConverter(Class<R> targetType) {
		return new StringToObjectNode<>(targetType);
	}

	private class StringToObjectNode<T extends ObjectNode> implements Converter<String, T> {

		private JsonMapper jsonMapper = JsonMapper.IGNORE_NULL;
		private Class<T> targetType;
		
		public StringToObjectNode(Class<T> targetType) {
			this.targetType = targetType;
		}

		@Override
		public T convert(String source) {
			T node = jsonMapper.fromJson(source, targetType);
			return node;
		}
		
	}

}
