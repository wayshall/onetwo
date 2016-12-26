package org.onetwo.common.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.onetwo.common.reflect.ReflectUtils;

public abstract class ParamUtils {

    public static String objectToParamString(Object obj){
    	return comparableKeyMapToParamString(ReflectUtils.getBeanToMapConvertor().toFlatMap(obj));
    }

    public static <T extends Comparable<T>> String comparableKeyMapToParamString(Map<T, ?> params){
    	return toParamString(params, Comparator.comparing(e->e));
    }
    public static String mapToParamString(Map<?, ?> params){
    	return toParamString(params, null);
    }
    
    public static <T> String toParamString(Map<T, ?> params, Comparator<T> comparator){
		Map<T, Object> map = null;
		if(comparator==null){
			map = new HashMap<>(params);
		}else{
			map = new TreeMap<>(comparator);
			map.putAll(params);
//			System.out.println("sortmap:"+map);
		}
		
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(Map.Entry<T, Object> entry : (Set<Map.Entry<T, Object>>)map.entrySet()){
			if(entry.getValue()==null)
				continue;
			Collection<?> values = CUtils.toCollection(entry.getValue());
			for(Object value : values){
				if(index!=0)
					sb.append("&");
				sb.append(entry.getKey()).append("=").append(value.toString());
				index++;
			}
		}
		return sb.toString();
	}
}
