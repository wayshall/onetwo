package org.onetwo.common.utils;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.onetwo.common.exception.BaseException;
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
	
	/***
	 * from java8 collectors
	 * @author wayshall
	 * @return
	 */
	public static <T> BinaryOperator<T> throwingMerger() {
        return (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }
	
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> arrayToMap(Object... arrays){
		 return Stream.iterate(0, i->i+2)
				 .limit(arrays.length/2)
				 .map(i->new Object[]{arrays[i], arrays[i+1]})
				 .collect(Collectors.toMap(array->(K)array[0], array->(V)array[1]));
//		 return Collections.EMPTY_MAP;
	}
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> kvArrayToMap(K[] keys, V[] values){
		 return Stream.iterate(0, i->i+1)
				 .limit(keys.length)
				 .map(i->new Object[]{keys[i], values[i]})
				 .collect(Collectors.toMap(array->(K)array[0], array->(V)array[1]));
	}
	
	public static <K, V> Map<K, V> toMap(List<V> data, Function<V, K> keyExtractor){
		 return data.stream()
				 	.collect(Collectors.toMap(item->keyExtractor.apply(item), item->item));
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
	
	public static <T> void splitTaskAndConsume(List<T> taskList, int taskSize, Consumer<List<T>> subTaskDataConsumer, boolean parallel) {
		splitTaskAndConsume(taskList, taskSize, (subTasks, taskIndex) -> {
			subTaskDataConsumer.accept(subTasks);
		}, parallel);
	}
	
	public static <T> void splitTaskAndConsume(List<T> taskList, int taskSize, BiConsumer<List<T>, Integer> subTaskDataConsumer) {
		splitTaskAndConsume(taskList, taskSize, subTaskDataConsumer, false);
	}
	public static <T> void splitTaskAndConsume(List<T> taskList, int taskSize, BiConsumer<List<T>, Integer> subTaskDataConsumer, boolean parallel) {
		long maxSize = taskList.size()%taskSize==0?taskList.size()/taskSize:(taskList.size()/taskSize+1);
		Stream<Integer> stream = Stream.iterate(0, t -> t+1).limit(maxSize);
		if (parallel) {
			stream = stream.parallel();	
		}
		stream.forEach(taskIndex -> {
			List<T> subTaskDatas = taskList.stream().skip(taskIndex * taskSize).limit(taskSize).collect(Collectors.toList());
			subTaskDataConsumer.accept(subTaskDatas, taskIndex);
		});
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
	public static void ntimesRun(Integer times, Consumer<Integer> consumer){
		ntimesRun(null, times, consumer);
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
	
	/***
	 * parse to b
	 * @author weishao zeng
	 * @param size
	 * @return
	 */
	public static int parseSize(String size) {
		return parseSize(size, null);
	}
	
	/***
	 * parse size to byte
	 * @author weishao zeng
	 * @param size
	 * @param def
	 * @return
	 */
	public static int parseSize(String size, Integer def) {
		if(StringUtils.isBlank(size)){
			if(def!=null){
				return def;
			}else{
				Assert.hasLength(size, "Size must not be empty");
			}
		}
		size = size.toLowerCase();
		if (size.endsWith("kb")) {
			return Integer.valueOf(size.substring(0, size.length() - 2)) * 1024;
		}else if (size.endsWith("mb")) {
			return Integer.valueOf(size.substring(0, size.length() - 2)) * 1024 * 1024;
		}else if (size.endsWith("gb")) {
			return Integer.valueOf(size.substring(0, size.length() - 2)) * 1024 * 1024* 1024;
		}else {
			//fix: 补充兼容不写b的写法
			int strimCount = 1;
			if (size.endsWith("k")) {
				return Integer.valueOf(size.substring(0, size.length() - strimCount)) * 1024;
			}else if (size.endsWith("m")) {
				return Integer.valueOf(size.substring(0, size.length() - strimCount)) * 1024 * 1024;
			}else if (size.endsWith("g")) {
				return Integer.valueOf(size.substring(0, size.length() - strimCount)) * 1024 * 1024* 1024;
			}
		}
		return Integer.valueOf(size);
	}

	public static long timeToSeconds(String time, long def) {
		return parseTime(time, def, (duration, timeUnit)->timeUnit.toSeconds(duration));
	}
	public static long timeToMills(String time, long def) {
		return parseTime(time, def, (duration, timeUnit)->timeUnit.toMillis(duration));
	}
	public static long timeToMinutes(String time, long def) {
		return parseTime(time, def, (duration, timeUnit)->timeUnit.toMinutes(duration));
	}
	
	/***
	 * 
	 * @author weishao zeng
	 * @param time
	 * @param def 当time为空时，会返回默认值def，def少于0时，则表示没有提供默认值，直接抛错
	 * @param convert
	 * @return
	 */
	public static long parseTime(String time, long def, BiFunction<Integer, TimeUnit, Long> convert) {
		if(StringUtils.isBlank(time)){
			if (def>=0) {
				return def;
			} else {
				throw new BaseException("time is blank and default value has not be provided.");
			}
		}
		Pair<Integer, TimeUnit> tu = parseTimeUnit(time);
		return convert.apply(tu.getKey(), tu.getValue());
		/*if(StringUtils.isBlank(time)){
			return def;
		}
		time = time.toLowerCase();
		TimeUnit timeUnit = null;
		if (time.endsWith("mis")) {
			timeUnit = TimeUnit.MILLISECONDS;
		}else if (time.endsWith("s")) {
			timeUnit = TimeUnit.SECONDS;
		}else if (time.endsWith("m")) {
			timeUnit = TimeUnit.MINUTES;
		}else if (time.endsWith("h")) {
			timeUnit = TimeUnit.HOURS;
		}else if (time.endsWith("d")) {
			timeUnit = TimeUnit.DAYS;
		}
		if(timeUnit!=null){
			int duration = Integer.valueOf(time.substring(0, time.length() - 1));
//			return timeUnit.toMillis(numb);
			return convert.apply(duration, timeUnit);
		}
		return Integer.valueOf(time);*/
	}
	
	/***
	 * default is seconds
	 * @author wayshall
	 * @param time
	 * @return
	 */
	public static Pair<Integer, TimeUnit> parseTimeUnit(String time) {
		time = Objects.requireNonNull(time).toLowerCase();
		TimeUnit timeUnit = null;
		int unitLength = 1;
		int times = 1;
		if (time.endsWith("mis")) {
			timeUnit = TimeUnit.MILLISECONDS;
			unitLength = 3;
		}else if (time.endsWith("mcs")) {
			timeUnit = TimeUnit.MICROSECONDS;
			unitLength = 3;
		}else if (time.endsWith("s")) {
			timeUnit = TimeUnit.SECONDS;
		}else if (time.endsWith("m")) {
			timeUnit = TimeUnit.MINUTES;
		}else if (time.endsWith("h")) {
			timeUnit = TimeUnit.HOURS;
		}else if (time.endsWith("d")) {
			timeUnit = TimeUnit.DAYS;
		}else if (time.endsWith("month")) {//月
			timeUnit = TimeUnit.DAYS;
			times = 30;//按30天算
		}else if (time.endsWith("y")) {//年
			timeUnit = TimeUnit.DAYS;
			times = 365;
		}else{
			unitLength = 0;
			timeUnit = TimeUnit.SECONDS;
		}
		int duration = Integer.valueOf(time.substring(0, time.length() - unitLength)) * times;
		return Pair.of(duration, timeUnit);
	}
	

	static public Color parseColor(String color){
		String[] strs = GuavaUtils.split(color, ",");
		return new Color(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]), Integer.parseInt(strs[2]));
	}

    @SafeVarargs
	public static <T> List<T> asList(T...values) {
		if (LangUtils.isEmpty(values)) {
			return Collections.emptyList();
		}
		return Arrays.asList(values);
	}
    
	public static <T extends Comparable<? super T>> T max(Collection<T> datas, T def) {
		T max = datas.stream().max(Comparator.naturalOrder()).orElse(def);
		return max;
	}
	
	public static <T> Predicate<T> or(Collection<? extends Predicate<T>> collection) {
		Predicate<T> predicate = null;
		for (Predicate<T> p : collection) {
			if (predicate==null) {
				predicate = p;
			} else {
				predicate = predicate.or(p);
			}
			
		}
		return predicate;
	}
	
	public static <T> Predicate<T> and(Collection<? extends Predicate<T>> collection) {
		Predicate<T> predicate = null;
		for (Predicate<T> p : collection) {
			if (predicate==null) {
				predicate = p;
			} else {
				predicate = predicate.and(p);
			}
			
		}
		return predicate;
	}
	
	private LangOps(){}

}
