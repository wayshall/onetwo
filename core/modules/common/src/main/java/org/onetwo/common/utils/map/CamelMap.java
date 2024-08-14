package org.onetwo.common.utils.map;

import java.util.LinkedHashMap;
import java.util.Map;

import org.onetwo.common.utils.StringUtils;

import com.google.common.collect.Maps;

public class CamelMap<V> extends CaseInsensitiveMap<String, V>{
	
	final private char sperator;
	
	public CamelMap() {
		super(new LinkedHashMap<>());
		this.sperator = '_';
	}
	
	public CamelMap(int initialCapacity, char sperator) {
		super(Maps.newLinkedHashMapWithExpectedSize(initialCapacity));
		this.sperator = sperator;
	}

	public CamelMap(int initialCapacity) {
		this(Maps.newLinkedHashMapWithExpectedSize(initialCapacity), '_');
	}
	
	public CamelMap(Map<String, V> map, char sperator){
		super(map);
		this.sperator = sperator;
	}
	
	protected Object convertKey(Object key){
		if(key instanceof String){
			key = StringUtils.toCamel(key.toString(), sperator, false);
		}else{
			throw new IllegalArgumentException("key must be a string : " + key);
		}
		return key;
	}
}
