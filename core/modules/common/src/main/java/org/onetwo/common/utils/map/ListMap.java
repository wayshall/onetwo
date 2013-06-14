package org.onetwo.common.utils.map;

import java.util.LinkedHashMap;
import java.util.List;

import org.onetwo.common.utils.LangUtils;

public class ListMap<K, V> extends LinkedHashMap<K, List<V>>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8313708595707811549L;


	public ListMap(){super();};
	
	
	public List<V> putElement(K key, V value){
		List<V> list = get(key);
		if(list==null){
			list = LangUtils.newArrayList();
			super.put(key, list);
		}
		list.add(value);
		return list;
	}
	
}
