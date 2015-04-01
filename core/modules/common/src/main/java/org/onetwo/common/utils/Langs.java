package org.onetwo.common.utils;

import java.util.Arrays;
import java.util.Map;

/***
 * langutils for java8
 * @author way
 *
 */
final public class Langs {
	
	public static Object[] toArray(Map<?, ?> map){
		return map.entrySet().stream().map(e -> Arrays.asList(e.getKey(), e.getValue())).flatMap(list -> list.stream()).toArray();
	}
	
//	public static long sum(Iterable<T>)
	
	private Langs(){}

}
