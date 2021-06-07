package org.onetwo.common.spring.converter;

import java.util.Map;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @author weishao zeng
 * <br/>
 */
public class StringToMapConverterFactory implements ConverterFactory<String, Map<String, Object>> {

	@Override
	public <R extends Map<String, Object>> Converter<String, R> getConverter(Class<R> targetType) {
		return new StringToMap<R>();
	}

	private class StringToMap<T extends Map<String, Object>> implements Converter<String, T> {

		private JsonMapper jsonMapper = JsonMapper.IGNORE_NULL;
		
		public StringToMap() {
			super();
		}

		@Override
		public T convert(String source) {
			if (StringUtils.isBlank(source)) {
				return null;
			}
			T map = jsonMapper.fromJson(source, Map.class);
			return map;
		}
		
	}

}
