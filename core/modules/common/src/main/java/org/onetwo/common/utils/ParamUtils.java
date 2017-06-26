package org.onetwo.common.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiFunction;

import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;

public abstract class ParamUtils {

	private static final BeanToMapConvertor BEAN_TO_MAP_CONVERTOR = BeanToMapBuilder.newBuilder()
																					.enableFieldNameAnnotation()
																					.build();
	
    public static String objectToParamString(Object obj){
    	return comparableKeyMapToParamString(BEAN_TO_MAP_CONVERTOR.toFlatMap(obj));
    }

    public static <T extends Comparable<T>> String comparableKeyMapToParamString(Map<T, ?> params){
    	return toParamString(params, Comparator.comparing(e->e));
    }
    /*public static String mapToParamString(Map<?, ?> params){
    	return toParamString(params, null);
    }*/

    public static <T> String toParamString(Map<T, ?> params, Comparator<T> comparator){
		Map<T, Object> map = null;
		if(comparator==null){
			map = new HashMap<>(params);
		}else{
			map = new TreeMap<>(comparator);
			map.putAll(params);
//			System.out.println("sortmap:"+map);
		}
		
		return toParamString(map);
	}

    @SuppressWarnings({ "rawtypes" })
	public static String toParamString(Map params){
		return toParamString(params, (BiFunction<Object, Object, String>)null);
	}
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static String toParamString(Map params, BiFunction<Object, Object, String> toStringFunc){
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(Map.Entry entry : (Set<Map.Entry>)params.entrySet()){
			if(entry.getValue()==null)
				continue;
			Collection<?> values = CUtils.toCollection(entry.getValue());
			for(Object value : values){
				if(index!=0)
					sb.append("&");
				if(toStringFunc!=null){
					sb.append(toStringFunc.apply(entry.getKey(), value));
				}else{
					sb.append(entry.getKey()).append("=").append(value.toString());
				}
				index++;
			}
		}
		return sb.toString();
	}

	public static String appendParam(String action, String name, String value){
		String result = action;
		if (action.indexOf("?")!=-1){
			result += "&"+name+"="+value;
		}else{
			result += "?"+name+"="+value;
		}
		return result;
	}
	
	public static String appendParamString(String action, String paramstr){
		String result = action;
		if (action.indexOf("?")!=-1){
			result += "&"+paramstr;
		}else{
			result += "?"+paramstr;
		}
		return result;
	}
}
