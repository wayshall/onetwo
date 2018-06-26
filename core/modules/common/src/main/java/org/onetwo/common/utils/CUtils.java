package org.onetwo.common.utils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.tuple.Pair;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.func.IndexableReturnableClosure;
import org.onetwo.common.utils.func.ReturnableClosure;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.Predicate;
import org.onetwo.common.utils.map.BaseMap;
import org.onetwo.common.utils.map.CollectionMap;

import com.google.common.collect.Maps;

@SuppressWarnings({ "rawtypes", "unchecked" })
final public class CUtils {


	public static class NullOrEmtpyPredicate implements Predicate {

		private boolean result;

		public NullOrEmtpyPredicate(boolean result) {
			this.result = result;
		}

		@Override
		public boolean apply(Object obj) {
			if (obj == null || (String.class.isAssignableFrom(obj.getClass()) && StringUtils.isBlank(obj.toString())))
				return result;
			return !result;
		}

	};

	public static NullOrEmtpyPredicate NullOrEmptyTrue = new NullOrEmtpyPredicate(true);

	public static NullOrEmtpyPredicate NullOrEmptyFalse = new NullOrEmtpyPredicate(false);
	
    public static final List NULL_LIST = new NullList();

    @SuppressWarnings("serial")
    public static class NullList extends AbstractList<Object> implements RandomAccess, Serializable {
    	
    	private NullList(){}
        public int size() {return 0;}

        public boolean contains(Object obj) {return false;}

        public Object get(int index) {
            return null;
        }

        // Preserves singleton property
        private Object readResolve() {
            return NULL_LIST;
        }
    }
    
    public static <T> Collection<T> emptyIfNull(Collection<T> col){
    	if(col==null)
    		return Collections.EMPTY_SET;
    	return col;
    }
    
	public static <K, V> Map<K, V> subtract(Map<K, V> first, Map<K, V> map){
		return subtract(first, map, false);
	}
	
	public static <K, V> Map<K, V> subtract(Map<K, V> first, Map<K, V> map, boolean modifyFirst){
		if(map==null || map.isEmpty())
			return first;
		Map rs = null;
		if(modifyFirst)
			rs = first;
		else{
			rs = newMap(first.getClass());
			rs.putAll(first);
		}
		for(Map.Entry entry : (Set<Map.Entry<K, V>>)map.entrySet()){
			rs.remove(entry.getKey());
		}
		return rs;
	}

	public static Map arrayIntoMap(Class mapClass, Object... params) {
		return arrayIntoMap(CUtils.newMap(mapClass), params);
	}
	
	public static <K, V> Map<K, V> newMap(Class<? extends Map> clazz){
		if(clazz==null)
			return new HashMap<K, V>();

		if(!Map.class.isAssignableFrom(clazz))
			throw new BaseException("class must be a map type: " + clazz);
		
		if(!clazz.isInterface()){
			return ReflectUtils.newInstance(clazz);
		}
		
		if(ConcurrentMap.class.isAssignableFrom(clazz)){
			return new ConcurrentHashMap<K, V>();
		}else if(SortedMap.class.isAssignableFrom(clazz)){
			return new TreeMap<K, V>();
		}else{
			return new HashMap<K, V>();
		}
	}
	

	public static <K, V> Map<K, V> asMap(Object... params) {
		if(LangUtils.isEmpty(params))
			return Collections.emptyMap();
		Map<K, V> map = arrayIntoMap(CUtils.newHashMap(-1), params);
		return map;
	}
	
	public static <T> Map<T, T> typeMap(T... params) {
		if(LangUtils.isEmpty(params))
			return Collections.emptyMap();
		Map<T, T> map = arrayIntoMap(CUtils.newHashMap(-1), params);
		return map;
	}


	public static Map<String, Object> fromProperties(Object obj) {
		return ReflectUtils.toMap(obj);
	}
	
	public static BaseMap toBase(Map map) {
		if(map instanceof BaseMap)
			return (BaseMap) map;
		if(map!=null)
			return new BaseMap(map);
		else
			return null;
	}
	
	public static <T extends Map> T arrayIntoMap(T properties, Object... params) {
		if (params.length % 2 == 1)
			throw new IllegalArgumentException("参数不是key, value形式！ ");

		int index = 0;
		Object name = null;
		for (Object s : params) {
			if (index % 2 == 0) {
				if (s == null || (String.class.isInstance(s) && StringUtils.isBlank(s.toString())))
					throw new IllegalArgumentException("字段名称不能为空！ ");
				name = s;
			} else {
				properties.put(name, s);
			}
			index++;
		}
		
		return properties;
	}
	
	public static <K, V> LinkedHashMap<K, V> asLinkedMap(Object...params){
		return CUtils.arrayIntoMap(CUtils.newLinkedHashMap(), params);
	}

	public static <T> List<T> newArrayList(){
		return newArrayList(-1);
	}
	
	public static <T> List<T> newArrayList(int size){
		ArrayList<T> list = null;
		if(size<=0)
			list = new ArrayList<T>();
		else
			list = new ArrayList<T>(size);
		return list;
	}
	
	public static <T> List<T> newList(T...objs){
		ArrayList<T> list = new ArrayList<T>(Arrays.asList(objs));
		return list;
	}
	
	public static <T> T[] newArray(T...objs){
		return objs;
	}

	public static <T> HashSet<T> newHashSet(){
		return new HashSet<T>();
	}

	public static <T> HashSet<T> asSet(T...objs){
		HashSet<T> set = new HashSet<T>();
		for(T obj : objs){
			set.add(obj);
		}
		return set;
	}

	public static <T> HashSet<T> newHashSet(int size){
		HashSet<T> sets = null;
		if(size<=0)
			sets = new HashSet<T>();
		else
			sets = new HashSet<T>(size);
		return sets;
	}
	public static <T> TreeSet<T> newTreeSet(){
		return new TreeSet<T>();
	}

	public static <K, V> Map<K, V> newMap(){
		return newHashMap(-1);
	}

	public static <K, V> Map<K, V> newHashMap(int size){
		if(size<=0)
			return new HashMap<K, V>();
		return new HashMap<K, V>(size);
	}

	public static <K, V> CollectionMap<K, V> newListMap(int size){
		if(size<=0)
			return CollectionMap.newListMap();
		return CollectionMap.newListMap(size);
	}

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int size){
		if(size<=0)
			return new LinkedHashMap<K, V>();
		return new LinkedHashMap<K, V>(size);
	}

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(){
		return new LinkedHashMap<K, V>();
	}

	public static <T> List<T> trimAsList(T... array) {
		return tolist(array, true);
	}
	public static <T> List<T> asList(T... array) {
		return tolist(array, false);
	}
	public static List tolist(Object object, boolean trimNull) {
		return tolist(object, trimNull, NULL_LIST);
	}
	
	public static <T> Collection<T> newCollections(Class<? extends Collection> clazz){
		return newCollections(clazz, null);
	}
	public static <T> Collection<T> newCollections(Class<? extends Collection> clazz, Integer size){
		if(clazz==null){
			return new ArrayList<T>();
		}
		if(!Collection.class.isAssignableFrom(clazz))
			throw new BaseException("class must be a Collection type: " + clazz);
		
		if(!clazz.isInterface()){
			return ReflectUtils.newInstance(clazz);
		}
		
		if(List.class.isAssignableFrom(clazz)){
			return size==null?new ArrayList<T>():new ArrayList<T>(size);
		}else if(SortedSet.class.isAssignableFrom(clazz)){
			return new TreeSet<T>();
		}else if(Set.class.isAssignableFrom(clazz)){
			return size==null?new HashSet<T>():new HashSet<T>(size);
		}else if(Queue.class.isAssignableFrom(clazz)){
			return size==null?new ArrayDeque<T>():new ArrayDeque<T>(size);
		}else{
			return size==null?new ArrayList<T>():new ArrayList<T>(size);
		}
	}

	public static <T> Collection<T> toCollection(Object object) {
		if(object==null)
			return Collections.emptyList();
		List list = null;
		if (Collection.class.isAssignableFrom(object.getClass())) {
			return (Collection<T>)object;
		} else if (object.getClass().isArray()) {
			int length = Array.getLength(object);
			list = new ArrayList(length);
			appendToList(object, list);
		} else {
			list = new ArrayList(5);
			list.add(object);
		}
		return list;
	}

	public static List tolist(Object object, boolean trimNull, List def) {
		if (object == null)
			return def;
		List list = null;
		if (List.class.isInstance(object)) {
			list = (List) object;
		} else if(Iterable.class.isInstance(object)){
			list = new ArrayList();
			for(Object obj : (Iterable<?>)object){
				list.add(obj);
			}
		}else if (object.getClass().isArray()) {
			int length = Array.getLength(object);
			list = new ArrayList(length);
//			appendToList(object, list);
			for (int i = 0; i < length; i++) {
				list.add(Array.get(object, i));
			}
		} else {
			list = new ArrayList(5);
			list.add(object);
		}
		if (trimNull)
			stripNull(list);
		return (list == null || list.isEmpty()) ? def : list;
	}
	
	public static void appendToList(Object object, List list){
		int length = Array.getLength(object);
		Object o = null;
		for(int i=0; i<length; i++){
			o = Array.get(object, i);
			if(o==null)
				continue;
			if(o.getClass().isArray())
				appendToList(o, list);
			else
				list.add(o);
		}
	}
	
	public static Collection stripNull(Collection collection) {
//		collection.removeIf(Objects::isNull);
		return strip(collection);
		/*collection.removeAll(Collections.singletonList(null));
		return collection;*/
	}

	public static <T> List<T> trimAndexcludeTheClassElement(boolean trimNull, Object array, Object... excludeClasses) {//Class... excludeClasses
		if (array == null)
			return NULL_LIST;
		
		List list = null;
		if(array.getClass().isArray()){
			int length = Array.getLength(array);
			list = new ArrayList(length);
			for (int i = 0; i < length; i++) {
				list.add(Array.get(array, i));
			}
		}else{
			list = tolist(array, trimNull);
		}
		
		if (excludeClasses!=null && excludeClasses.length>0)
			strip(list, (Object[]) excludeClasses);
		return (list == null) ? NULL_LIST : list;
	}
	
	/****
	 * remove specify value frome list
	 * default remove null or blank string
	 * @param collection
	 * @param stripValue
	 * @return
	 */
	public static Collection strip(Collection<?> collection, final Object... stripValue) {
		collection.removeIf(obj-> {

				if (obj instanceof Class) {
					if (ArrayUtils.isAssignableFrom(stripValue, (Class) obj))
						return true;
				} else if (obj == null || (String.class.isAssignableFrom(obj.getClass()) && StringUtils.isBlank(obj.toString())) || ArrayUtils.contains(stripValue, obj)){
					return true;
				}
				return false;
			
		});
		return collection;
		
	}
	
	public static Object[] asArray(Object obj) {
		if(obj.getClass().isArray()){
			return (Object[]) obj;
		}else if(Collection.class.isAssignableFrom(obj.getClass())){
			Collection col = (Collection) obj;
			Object[] array = new Object[col.size()];
			int i = 0;
			for (Object o : col) {
				array[i++] = o;
			}
			return array;
		}else{
			return new Object[]{obj};
		}
	}
	
	public static String[] asStringArray(Object obj) {
		if(obj.getClass().isArray()){
			if(obj.getClass().getComponentType()==String.class){
				return (String[]) obj;
			}else{
				Object[] objArray = (Object[]) obj;
				String[] strs = new String[objArray.length];
				int index = 0;
				for(Object o : objArray){
					strs[index++] = o.toString();
				}
				return strs;
			}
		}else if(Collection.class.isAssignableFrom(obj.getClass())){
			Collection col = (Collection) obj;
			String[] strs = new String[col.size()];
			int i = 0;
			for (Object o : col) {
				strs[i++] = o.toString();
			}
			return strs;
		}else if(Iterable.class.isAssignableFrom(obj.getClass())){
			List<String> list = LangUtils.newArrayList();
			for (Object o : (Iterable) obj) {
				list.add(o.toString());
			}
			return list.toArray(new String[0]);
		}else{
			return new String[]{obj.toString()};
		}
	}
	
	public static Object[] map2Array(Map<?, ?> map){
		Object[] array = new Object[map.size()*2];
		int index = 0;
		for(Entry<?, ?> entry : map.entrySet()){
			array[index++] = entry.getKey();
			array[index++] = entry.getValue();
		}
		return array;
	}
	
	public static <K, V> Map<K, Collection<V>> groupBy(Collection<V> datas, ReturnableClosure<V, K> block){
		return JFishList.wrap(datas).groupBy(block);
	}
	
	/***
	 * 
	 * @author wayshall
	 * @param collection
	 * @param predicate 返回true，则保留；返回false，则移除
	 */
    public static <T> void filter(Collection<T> collection, Predicate<T> predicate) {
    	CollectionUtils.filter(collection, predicate);
    }
	
    /***
     * 
     * @author wayshall
     * @param map
     * @param predicate 返回true，则保留；返回false，则移除
     */
    public static <K, V> void filter(Map<K, V> map, Predicate<Entry<K, V>> predicate) {
    	if (map != null && predicate != null) {
            for (Iterator<Entry<K, V>> it = map.entrySet().iterator(); it.hasNext();) {
                if (predicate.apply(it.next()) == false) {
                    it.remove();
                }
            }
        }
    }
	
    /*****
     * list1中存在，list2中找不到的元素
     * @param list1
     * @param list2
     * @param notInPredicate
     */
    public static <T> List<T> difference(List<T> list1, List<T> list2, NotInPredicate<T> notInPredicate) {
    	List<T> diff = LangUtils.newArrayList();
    	for(T e : list1){
    		if(notInPredicate.apply(e, list2))
    			diff.add(e);
    	}
       return Collections.unmodifiableList(diff);
    }
    /****
     * 
     * list1中存在，list2中找不到的元素
     * @param list1
     * @param list2
     * @param properties
     * @return
     */
    public static <T> List<T> difference(List<T> list1, List<T> list2, final String...properties) {
    	return difference(list1, list2, new NotInPredicate<T>() {
			@Override
			public boolean apply(T e, List<T> list) {
				return !contains(list, e, properties);
			}
		});
    }
	
	public static interface NotInPredicate<T> {
		boolean apply(T e, List<T> list);
	}
	
	public static interface EqualsPredicate<T> {
		boolean apply(T e1, T e2);
	}
	
	public static <T> boolean containsAnyOne(Collection<T> c, T...elements){
		Assert.notEmpty(elements);
		if(LangUtils.isEmpty(c))
			return false;
		for(T e : elements){
			if(c.contains(e))
				return true;
		}
		return false;
	}
	
	public static <T> boolean contains(Collection<T> c, T element, EqualsPredicate<T> equalsPredicate){
		if(LangUtils.isNotEmpty(c)){
			for(T e : c){
				if(equalsPredicate.apply(e, element))
					return true;
			}
		}
		return false;
	}
	
	public static <T> boolean contains(Collection<T> c, T element, final String...properties){
		return contains(c, element, new EqualsPredicate<T>(){
			@Override
			public boolean apply(T e1, T e2) {
				return isEquals(e1, e2, properties);
			}
		});
	}

	
	public static <T> boolean isEquals(T e1, T e2, EqualsPredicate<T> equalsPredicate){
		return equalsPredicate.apply(e1, e2);
	}
	
	public static <T> boolean isEquals(final T e1, final T e2, final String...properties){
		if(e1==e2)
			return true;
		if(e1==null || e2==null)
			return false;
		return isEquals(e1, e2, new EqualsPredicate<T>(){

			@Override
			public boolean apply(T e1, T e2) {
				for(String p : properties){
					if(!ReflectUtils.getPropertyValue(e1, p).equals(ReflectUtils.getPropertyValue(e2, p)))
						return false;
				}
				return true;
			}
			
		});
	}
	

	
	public static Map<Integer, Object> toMap(List<?> list){
		return toMap(list, (e, index)->index);
	}
	
	public static <K> Map<K, Object> toMap(List<?> list, IndexableReturnableClosure<Object, K> keyMap){
		Map<K, Object> map = Maps.newHashMap();
		int index = 0;
		for(Object e : list){
			K key = keyMap.execute(e, index);
			map.put(key, e);
			index++;
		}
		return map;
	}

	public static List<Object> toList(Map<?, ?> map) {
		if(map==null)
			return NULL_LIST;
		List<Object> list = new ArrayList(map.size()*2);
		for(Map.Entry<?, ?> entry : map.entrySet()){
			list.add(entry.getKey());
			list.add(entry.getValue());
		}
		return list;
	}

	public static <T, R> List<R> map(Collection<T> list, Function<? super T, ? extends R> mapper){
		Assert.notNull(list);
		Assert.notNull(mapper);
		return list.stream().map(mapper).collect(Collectors.toList());
	}
	
	public static <T> List<T> iterableToList(Iterable<T> it){
		if(it==null)
			return Collections.EMPTY_LIST;
		/*List<T> list = new ArrayList<T>();
		it.forEach(e->list.add(e));
		return Collections.unmodifiableList(list);*/
		return StreamSupport.stream(it.spliterator(), false)
							.collect(Collectors.toList());
	}
	
	public static <T> Optional<Pair<Integer, T>> findByClass(List<?> list, Class<T> targetClass){
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Object element = list.get(i);
			if(element!=null && targetClass.isAssignableFrom(ReflectUtils.getObjectClass(element))){
				Pair res = Pair.of(i, (T)element);
				return Optional.of(res);
			}
		}
		return Optional.empty();
	}
	
	public static void replaceOrAdd(List list, Class targetClass, Object element){
		Optional<Pair<Integer, Object>> targetElement = CUtils.findByClass(list, targetClass);
		if(targetElement.isPresent()){
			list.set(targetElement.get().getKey(), element);
		}else{
			list.add(element);
		}
	}
	
	private CUtils(){
	}

}
