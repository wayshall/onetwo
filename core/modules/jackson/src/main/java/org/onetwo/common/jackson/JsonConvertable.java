package org.onetwo.common.jackson;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weishao zeng
 * <br/>
 */
public interface JsonConvertable {
	

	Object getConvertableValue();
	
	default <T> T asJson(Class<T> clazz) {
		return JsonMapper.IGNORE_EMPTY.fromJson(getConvertableValue(), clazz);
	}
	
	default Map<String, Object> asMap() {
		return JsonMapper.IGNORE_EMPTY.fromJson(getConvertableValue(), HashMap.class);
	}

}
