package org.onetwo.common.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.func.Closure;
import org.onetwo.common.utils.map.KVEntry;

/***
 * langutils for java8
 * @author way
 *
 */
final public class LangOps {
	
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> arrayToMap(Object... arrays){
		 return Stream.iterate(0, i->i+2)
				 .limit(arrays.length/2)
				 .map(i->new Object[]{arrays[i], arrays[i+1]})
				 .collect(Collectors.toMap(array->(K)array[0], array->(V)array[1]));
//		 return Collections.EMPTY_MAP;
	}
	public static <K, V> Map<K, V> kvArrayToMap(K[] keys, V[] values){
		 return Stream.iterate(0, i->i+1)
				 .limit(keys.length)
				 .map(i->new Object[]{keys[i], values[i]})
				 .collect(Collectors.toMap(array->(K)array[0], array->(V)array[1]));
	}
	
	public static Object[] toArray(Map<?, ?> map){
		return map.entrySet().stream().map(e -> Arrays.asList(e.getKey(), e.getValue())).flatMap(list -> list.stream()).toArray();
	}
	
	@SuppressWarnings("unchecked")
	public static <T, R> R[] toArray(List<T> list, Function<? super T, ? extends R> mapper){
		return (R[])list.stream().map(mapper).collect(Collectors.toList()).toArray();
	}
	
	public static <R> Map<Long, R> generateMap(Long count, Function<Long, ? extends R> mapper){
		return Stream.iterate(1L, i->i+1)
					.limit(count)
					.map(i->new KVEntry<Long, R>(i, mapper.apply(i)))
					.collect(Collectors.toMap(e->e.getKey(), e->e.getValue()));
	}
	
	public static <R> List<R> generateList(int count, Function<Integer, ? extends R> mapper){
		return Stream.iterate(1, i->i+1)
						.limit(count)
						.map(mapper)
						.collect(Collectors.toList());
	}
	public static <R> List<R> generateList(Long count, Function<Long, ? extends R> mapper){
		return Stream.iterate(1L, i->i+1)
						.limit(count)
						.map(mapper)
						.collect(Collectors.toList());
	}
	
	public static void timeIt(String tag, Closure closure){
		
	}
	public static void timeIt(String tag, Integer times, Closure closure){
		TimeCounter tc = new TimeCounter(tag);
		tc.start();
		try {
			for (int i = 0; i < times; i++) {
				closure.execute();
			}
		} finally{
			tc.stop();
		}
	}

	public static void ntimesRun(Integer times, Closure closure){
		ntimesRun(null, times, closure);
	}
	
	public static void ntimesRun(String printTimeTag, Integer times, Closure closure){
		ntimesRun(printTimeTag, times, i->closure.execute());
	}
	public static void ntimesRun(String printTimeTag, Integer times, Consumer<Integer> consumer){
		TimeCounter t = StringUtils.isNotBlank(printTimeTag)?new TimeCounter(printTimeTag).startIt():null;
		Stream.iterate(1, i->i+1).limit(times)
									.forEach(i->{
										consumer.accept(i);
									});
		if(t!=null){
			t.stop();
		}
	}
	
	public static <R> List<R> ntimesMap(Integer times, Function<Integer, R> mapper){
		return Stream.iterate(1, i->i+1).limit(times)
									.map(mapper)
									.collect(Collectors.toList());
	}
	
//	public static long sum(Iterable<T>)
	
	public static <T> BigDecimal sumBigDecimal(List<T> datas, Function<T, BigDecimal> mapper){
		if(datas==null)
			return BigDecimal.valueOf(0.0);
		return datas.stream().map(mapper).reduce((n1, n2)->{
			return n1.add(n2);
		})
		.orElse(BigDecimal.valueOf(0.0));
	}

	@SuppressWarnings("unchecked")
	public static <T, K> Map<K, List<T>> groupByProperty(List<T> datas, String propName){
		return datas.stream().collect(Collectors.groupingBy(e->(K)ReflectUtils.getPropertyValue(e, propName)));
	}
	
	@SuppressWarnings("unchecked")
	public static <T, K, V> Map<K, List<V>> groupByProperty(List<T> datas, String keyProperty, String valueProperty){
		return groupBy(datas, e->(K)ReflectUtils.getPropertyValue(e, keyProperty), e->(V)ReflectUtils.getPropertyValue(e, valueProperty));
	}
	
	public static <T, K> Map<K, List<T>> groupBy(List<T> datas, Function<? super T, ? extends K> keyer){
		return datas.stream().collect(Collectors.groupingBy(keyer));
	}
	public static <T, K, V> Map<K, List<V>> groupBy(List<T> datas, Function<? super T, ? extends K> keyer, Function<? super T, ? extends V> valuer){
		Map<K, List<V>> groups = datas.stream()
										.collect(Collectors.groupingBy(
												keyer, 
												Collectors.mapping(valuer, Collectors.toList())
											)
										);
		return groups;
	}
	
	private LangOps(){}

}
