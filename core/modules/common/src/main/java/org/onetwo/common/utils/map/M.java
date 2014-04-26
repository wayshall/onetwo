package org.onetwo.common.utils.map;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.L;
import org.onetwo.common.utils.map.MapFun.SimpleMapFun;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class M {

	public static Map<Object, Object> fromProperties(Object obj) {
		return CUtils.fromProperties(obj);
	}
	
	public static Map<Object, Object> fromProperties(Object obj, boolean craeteIfNull) {
		return CUtils.fromProperties(obj, craeteIfNull);
	}
	
	public static Map<Object, Object> from(Object obj) {
		return from(obj, false);
	}
	
	public static Map<Object, Object> from(Object obj, boolean craeteIfNull) {
		return CUtils.from(obj, craeteIfNull);
	}

	public static Map bean2Map(Object obj, Object... ignores) {
		return CUtils.bean2Map(obj, ignores);
	}
	
	public static boolean hasElement(Map map){
		return !(map==null || map.isEmpty());
	}
	
	public static BaseMap toBase(Map map) {
		return CUtils.toBase(map);
	}

	public static Object[] toArray(Map map) {
		if(map==null)
			return null;
		return L.toArray(map).toArray();
	}

	public static Map c(Object... params) {
		return CUtils.asMap(params);
	}

	public static Map c(Collection params) {
		return CUtils.asMap(params.toArray());
	}

	public static Map map(boolean createMap, Object... params) {
		if(params==null && !createMap)
			return null;
		Map map = map((Class)null, params);
		return map;
	}
	

	public static Map map(Class mapClass, Object... params) {
		return map(CUtils.newMap(mapClass), params);
	}

	public static <T extends Map> T map(T properties, Object... params) {
		return CUtils.arrayIntoMap(properties, params);
	}
	
	/*public static Map newMap(){
		return new HashMap();
	}*/
	/*
	public static Map newMap(Class mapClass){
		if(mapClass==null)
			return LangUtils.newHashMap();
		return (Map)ReflectUtils.newInstance(mapClass);
	}*/
	
	public static void filterNull(final Map params){
		new EasyMap(params).each(new SimpleMapFun(){

			@Override
			public void doMap(Entry element, int index, Object... objects) {
				if(element.getValue()==null)
					getThis().getIterator().remove();
			}
			
		});
	}
	
	public static void filterBlank(final Map params){

		new EasyMap(params).each(new SimpleMapFun(){

			@Override
			public void doMap(Entry element, int index, Object... objects) {
				if(element.getValue()==null || StringUtils.isBlank(element.getValue().toString()))
					getThis().getIterator().remove();
			}
			
		});
	}
	


	/*public static Map subtract(Map first, Map map){
		return subtract(first, map, false);
	}
	
	public static Map subtract(Map first, Map map, boolean modifyFirst){
		if(map==null || map.isEmpty())
			return first;
		Map rs = null;
		if(modifyFirst)
			rs = first;
		else{
			rs = M.newMap(first.getClass());
			rs.putAll(first);
		}
		for(Map.Entry entry : (Set<Map.Entry>)map.entrySet()){
			rs.remove(entry.getKey());
		}
		return rs;
	}*/
}
