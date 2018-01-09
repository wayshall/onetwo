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
		return generateKey(keyParamList.toArray());
	}
	
}
