package org.onetwo.common.spring.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

public class MethodKeyGenerator extends SimpleKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		List<Object> keyParamList = new ArrayList<Object>(3+params.length);
		/*if(target!=null){
			keyParamList.add(target);
		}*/
		keyParamList.add(method.toGenericString());
		keyParamList.addAll(Arrays.asList(params));
//		return generateKey(keyParamList.toArray());
		return generateMethodKey(keyParamList.toArray());
	}
	
	public static Object generateMethodKey(Object... params) {
		if (params.length == 0) {
			return MethodSimpleKey.EMPTY;
		}
		if (params.length == 1) {
			Object param = params[0];
			if (param != null && !param.getClass().isArray()) {
				return param;
			}
		}
		MethodSimpleKey key = new MethodSimpleKey();
		key.setParams(params);
		return key;
	}
	
}
