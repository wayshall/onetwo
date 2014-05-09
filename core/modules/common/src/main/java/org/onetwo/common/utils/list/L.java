package org.onetwo.common.utils.list;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class L {

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

	public static class StripValuePredicate implements Predicate {

		private final Object[] stripValue;
		private boolean result;

		public StripValuePredicate(boolean result, Object... stripValue) {
			this.stripValue = stripValue;
			this.result = result;
		}

		@Override
		public boolean apply(Object obj) {
			if (obj instanceof Class) {
				if (ArrayUtils.isAssignableFrom(stripValue, (Class) obj))
					return result;
			} else if (obj == null || (String.class.isAssignableFrom(obj.getClass()) && StringUtils.isBlank(obj.toString())) || ArrayUtils.contains(stripValue, obj))
				return result;
			return !result;
		}

	};
	

	public static List<Long> nList(Long start, Long end) {
		return aslist(start, end, Long.class);
	}

	public static <T> List<T> nList(Long start, Long end, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		T val = null;
		for(Long i=start; i<end; i++){
			val = MyUtils.simpleConvert(i, clazz);
			list.add(val);
		}
		return list;
	}

	public static List<Integer> nList(Integer start, Integer end) {
		Assert.notNull(start);
		Assert.notNull(end);
		if(start>end)
			throw new ServiceException("start must less than end:"+start+","+end);
		return nList(start.longValue(), end.longValue(), Integer.class);
	}


	public static List aslist(Object... array) {
		return list(true, array);
	}

	public static List aslistIfNull(boolean createIfNull, Object... array) {
		List rs = list(createIfNull, array);
		if(rs==null)
			rs = newList();
		return rs;
	}

	public static List list(boolean trimNull, Object... array) {
		return tolist(array, trimNull);
	}
	
	public static List newList(){
		return new ArrayList();
	}
	
	public static List IfNullCreate(Object obj){
		return IfNullCreate(obj, false);
	}
	
	public static List IfNullCreate(Object obj, boolean addElementToList){
		List list = tolist(obj, true);
		List newlist = null;
		if(list==null){
			newlist = newList();
			if(addElementToList)
				newlist.add(obj);
		}else{
			newlist = list;
		}
		return newlist;
	}
	
	public static List newList(Class listClass){
		if(listClass==null)
			return newList();
		return (List)ReflectUtils.newInstance(listClass);
	}
	

	public static List tolist(Object object, boolean trimNull) {
		return CUtils.tolist(object, trimNull, NULL_LIST);
	}

	public static List tolist(Object object, boolean trimNull, List def) {
		return CUtils.tolist(object, trimNull, def);
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

    
    public static final List NULL_LIST = CUtils.NULL_LIST;
    
    
	public static List exclude(Object array, Object... excludeClasses) {//Class... excludeClasses
    	return trimAndexcludeTheClassElement(true, array, excludeClasses);
    }

	@Deprecated
	public static List trimAndexcludeTheClassElement(boolean trimNull, Object array, Object... excludeClasses) {//Class... excludeClasses
		return CUtils.trimAndexcludeTheClassElement(trimNull, array, excludeClasses);
	}

	@Deprecated
	public static Collection stripNull(Collection collection) {
		return CUtils.strip(collection);
	}
	
	@Deprecated
	public static Collection strip(Collection collection, final Object... stripValue) {
		return CUtils.strip(collection, stripValue);
	}

	public static List toArray(Map map) {
		if(map==null)
			return null;
		List list = new ArrayList();
		for(Map.Entry entry : (Set<Map.Entry>)map.entrySet()){
			list.add(entry.getKey());
			list.add(entry.getValue());
		}
		return list;
	}

}
