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
	
	public static final String URL_PARAM_JOINER = "&";

	private static final BeanToMapConvertor BEAN_TO_MAP_CONVERTOR = BeanToMapBuilder.newBuilder()
																					.enableFieldNameAnnotation()
																					.build();
	
    public static String objectToParamString(Object obj){
    	return comparableKeyMapToParamString(BEAN_TO_MAP_CONVERTOR.toFlatMap(obj));
    }

    public static <T extends Comparable<T>> String comparableKeyMapToParamString(Map<T, ?> params){
    	return comparableKeyMapToParamString(params, URL_PARAM_JOINER);
    }
    public static <T extends Comparable<T>> String comparableKeyMapToParamString(Map<T, ?> params, String joiner){
    	return toParamString(params, Comparator.comparing(e->e), joiner, null);
    }
    public static <T extends Comparable<T>> String comparableKeyMapToParamString(Map<T, ?> params, String joiner, BiFunction<T, Object, String> toStringFunc){
    	return toParamString(params, Comparator.comparing(e->e), joiner, toStringFunc);
    }
    /*public static String mapToParamString(Map<?, ?> params){
    	return toParamString(params, null);
    }*/

    public static <T> String toParamString(Map<T, ?> params, Comparator<T> comparator){
    	return toParamString(params, comparator, URL_PARAM_JOINER, null);
    }
    public static <T> String toParamString(Map<T, ?> params, Comparator<T> comparator, String joiner, BiFunction<T, Object, String> toStringFunc){
		Map<T, Object> map = null;
		if(comparator==null){
			map = new HashMap<>(params);
		}else{
			map = new TreeMap<>(comparator);
			map.putAll(params);
//			System.out.println("sortmap:"+map);
		}
		
		return toParamString(map, toStringFunc, joiner);
	}

	public static <K> String toParamString(Map<K, Object> params){
		return toParamString(params, (BiFunction<K, Object, String>)null);
	}

	public static <K> String toParamString(Map<K, Object> params, String joiner){
		return toParamString(params, (BiFunction<K, Object, String>)null, joiner);
	}

	public static <K> String toParamString(Map<K, Object> params, BiFunction<K, Object, String> toStringFunc){
		return toParamString(params, toStringFunc, URL_PARAM_JOINER);
	}
	
	public static <K> String toParamString(Map<K, Object> params, BiFunction<K, Object, String> toStringFunc, String joiner){
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(Map.Entry<K, Object> entry : (Set<Map.Entry<K, Object>>)params.entrySet()){
			if(entry.getValue()==null)
				continue;
			Collection<?> values = CUtils.toCollection(entry.getValue());
			for(Object value : values){
				if(index!=0 && joiner.length()>0)
					sb.append(joiner);
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
			result += URL_PARAM_JOINER+name+"="+value;
		}else{
			result += "?"+name+"="+value;
		}
		return result;
	}
	
	public static String appendParamString(String action, String paramstr){
		String result = action;
		if (action.indexOf("?")!=-1){
			result += URL_PARAM_JOINER+paramstr;
		}else{
			result += "?"+paramstr;
		}
		return result;
	}
}
