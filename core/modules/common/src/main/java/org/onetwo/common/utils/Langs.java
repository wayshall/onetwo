package org.onetwo.common.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * langutils for java8
 * @author way
 *
 */
final public class Langs {
	
	public static Object[] toArray(Map<?, ?> map){
		return map.entrySet().stream().map(e -> Arrays.asList(e.getKey(), e.getValue())).flatMap(list -> list.stream()).toArray();
	}
	
	public static <R> List<R> generateList(Integer count, Function<Integer, ? extends R> mapper){
		return Stream.iterate(1, i->i+1).limit(count)
										.map(mapper)
										.collect(Collectors.toList());
	}
	
//	public static long sum(Iterable<T>)
	
	private Langs(){}

}
