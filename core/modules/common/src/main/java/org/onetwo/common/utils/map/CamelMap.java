package org.onetwo.common.utils.map;

import org.onetwo.common.utils.StringUtils;

public class CamelMap<V> extends CaseInsensitiveMap<String, V>{
	
	final private char sperator;
	
	public CamelMap(int initialCapacity, char sperator) {
		super(initialCapacity);
		this.sperator = sperator;
	}

	public CamelMap(int initialCapacity) {
		this(initialCapacity, '_');
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
