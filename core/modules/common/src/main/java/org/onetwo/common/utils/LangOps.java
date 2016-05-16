package org.onetwo.common.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.func.Closure;

/***
 * langutils for java8
 * @author way
 *
 */
final public class LangOps {
	
	public static <K, V> Map<K, V> arrayToMap(Object... arrays){
		 return Stream.iterate(0, i->i+2)
				 .limit(arrays.length/2)
				 .map(i->new Object[]{arrays[i], arrays[i+1]})
				 .collect(Collectors.toMap(array->(K)array[0], array->(V)array[1]));
//		 return Collections.EMPTY_MAP;
	}
	
	public static Object[] toArray(Map<?, ?> map){
		return map.entrySet().stream().map(e -> Arrays.asList(e.getKey(), e.getValue())).flatMap(list -> list.stream()).toArray();
	}
	
	@SuppressWarnings("unchecked")
	public static <T, R> R[] toArray(List<T> list, Function<? super T, ? extends R> mapper){
		return (R[])list.stream().map(mapper).collect(Collectors.toList()).toArray();
	}
	
	public static <R> List<R> generateList(Integer count, Function<Integer, ? extends R> mapper){
		return Stream.iterate(1, i->i+1).limit(count)
										.map(mapper)
										.collect(Collectors.toList());
	}


	public static void timeIt(String tag, Closure closure){
		TimeCounter tc = new TimeCounter(tag);
		tc.start();
		try {
			closure.execute();
		} finally{
			tc.stop();
		}
	}

	public static void repeatRun(Integer times, Closure closure){
		repeatRun(null, times, closure);
	}
	
	public static void repeatRun(String printTimeTag, Integer times, Closure closure){
		TimeCounter t = StringUtils.isNotBlank(printTimeTag)?new TimeCounter(printTimeTag).startIt():null;
		Stream.iterate(1, i->i+1).limit(times)
									.forEach(i->{
										closure.execute();
									});
		if(t!=null){
			t.stop();
		}
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
	
	private LangOps(){}

}
