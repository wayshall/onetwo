package org.onetwo.common.utils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractList;
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
import java.util.RandomAccess;
import java.util.Set;
import java.util.TreeSet;

import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.L;
import org.onetwo.common.utils.list.Predicate;
import org.onetwo.common.utils.map.BaseMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
final public class CUtils {

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
	
	public static <K, V> Map<K, V> newMap(Class<? extends Map> mapClass){
		if(mapClass==null)
			return new HashMap<K, V>();
		return (Map<K, V>)ReflectUtils.newInstance(mapClass);
	}
	

	public static <K, V> Map<K, V> asMap(Object... params) {
		if(LangUtils.isEmpty(params))
			return Collections.EMPTY_MAP;
		Map map = arrayIntoMap(CUtils.newHashMap(-1), params);
		return map;
	}


	public static Map<Object, Object> fromProperties(Object obj) {
		return fromProperties(obj, false);
	}
	
	public static Map<Object, Object> fromProperties(Object obj, boolean craeteIfNull) {
		if(obj==null)
			return craeteIfNull?LangUtils.newHashMap():null;
		if(Map.class.isInstance(obj))
			return (Map) obj;
		PropertyDescriptor[] props = ReflectUtils.desribProperties(obj.getClass());
		Map<Object, Object> map = LangUtils.newHashMap();
		for(PropertyDescriptor prop : props){
			if(prop==null)
				continue;
			Object value = ReflectUtils.getProperty(obj, prop);
			if(value!=null)
				map.put(prop.getDisplayName(), value);
		}
		return map;
	}

	public static Map<Object, Object> from(Object obj, boolean craeteIfNull) {
		if(obj==null)
			return craeteIfNull?newMap():null;

		if(Map.class.isInstance(obj))
			return (Map) obj;
		PropertyDescriptor[] props = ReflectUtils.desribProperties(obj.getClass());
		Map<Object, Object> map = LangUtils.newHashMap();
		for(PropertyDescriptor prop : props){
			if(prop==null)
				continue;
			Object value = ReflectUtils.getFieldValue(obj, prop.getDisplayName(), false);
			if(value!=null)
				map.put(prop.getDisplayName(), value);
		}
		return map;
	}
	public static BaseMap toBase(Map map) {
		if(map instanceof BaseMap)
			return (BaseMap) map;
		if(map!=null)
			return new BaseMap(map);
		else
			return null;
	}
	
	public static Map bean2Map(Object obj, Object... ignores) {
		Assert.notNull(obj);
		if(Map.class.isInstance(obj))
			return (Map)obj;
		Ignore ig = null;
		if(LangUtils.hasElement(ignores))
			ig = Ignore.create(ignores);
		PropertyDescriptor[] props = ReflectUtils.desribProperties(obj.getClass());
		Map propMap = LangUtils.newHashMap();
		Object val = null;
		
		for(PropertyDescriptor prop : props){
			val = ReflectUtils.getProperty(obj, prop);
			if(ig!=null && ig.ignore(prop.getName(), val))
				continue;
//			val = ig.defaultVal(prop.getName(), val);
			propMap.put(prop.getName(), val);
		}
		return propMap;
	}
	public static <T extends Map> T arrayIntoMap(T properties, Object... params) {
		if (params.length % 2 == 1)
			throw new IllegalArgumentException("参数个数必须是偶数个！ ");

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
	
	public static LinkedHashMap<Object, Object> asOrCreateLinkedHashMap(Object...params){
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

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int size){
		if(size<=0)
			return new LinkedHashMap<K, V>();
		return new LinkedHashMap<K, V>(size);
	}

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(){
		return new LinkedHashMap<K, V>();
	}

	public static List tolist(Object object, boolean trimNull) {
		return tolist(object, trimNull, NULL_LIST);
	}

	public static <T> Collection<T> toCollection(Object object) {
		if(object==null)
			return Collections.EMPTY_LIST;
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
		if (Collection.class.isAssignableFrom(object.getClass())) {
			if (List.class.isAssignableFrom(object.getClass()))
				list = (List) object;
			else {
				Collection col = (Collection) object;
				if(col.isEmpty()){
					list = new ArrayList(5);
				}else{
					list = new ArrayList(col.size());
					list.addAll(col);
				}
			}
		} else if (object.getClass().isArray()) {
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
		return strip(collection);
	}
	
	public static Collection strip(Collection collection, final Object... stripValue) {
		L.StripValuePredicate stripPredicate = new L.StripValuePredicate(false, stripValue);
		CollectionUtils.filter(collection, stripPredicate);//remove if return false
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
	
	public static <K, V> List<Pair<K, V>> map2List(Map<K, V> map){
		List<Pair<K, V>> list = newArrayList(LangUtils.isEmpty(map)?3:map.size());
		Pair<K, V> p = null;
		for(Entry<?, ?> entry : map.entrySet()){
			p = new Pair(entry.getKey(), entry.getValue());
			list.add(p);
		}
		return list;
	}
	
	public static <K, V> Map<K, List<V>> groupBy(Collection<V> datas, SimpleBlock<V, K> block){
		return JFishList.wrap(datas).groupBy(block);
	}
	
    public static <T> void filter(Collection<T> collection, Predicate<T> predicate) {
    	CollectionUtils.filter(collection, predicate);
    }
	
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
     * @param predicate
     */
    public static <T> List<T> difference(List<T> list1, List<T> list2, NotInPredicate<T> notInPredicate) {
    	List<T> diff = LangUtils.newArrayList();
    	for(T e : list1){
    		if(notInPredicate.apply(e, list2))
    			diff.add(e);
    	}
       return Collections.unmodifiableList(diff);
    }
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
					if(!ReflectUtils.getProperty(e1, p).equals(ReflectUtils.getProperty(e2, p)))
						return false;
				}
				return true;
			}
			
		});
	}
	
	private CUtils(){
	}

}
