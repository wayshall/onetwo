package org.onetwo.common.utils.map;

import java.util.LinkedHashMap;
import java.util.Map;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.MyUtils;

@SuppressWarnings("serial")
public class BaseMap<K, V> extends LinkedHashMap<K, V> {
	
	public BaseMap(){super();};
	
	public BaseMap(Map<K, V> map){
		super(map);
	}

	public Object get(Object key, Object def){
		Object val = get(key);
		if(val==null)
			return def;
		return val;
	}

	public K getKey(V val){
		for(Map.Entry<K, V> entry : this.entrySet()){
			if(val==null && val==entry.getValue())
				return entry.getKey();
			else if(val!=null && val.equals(entry.getValue()))
				return entry.getKey();
		}
		return null;
	}
	
	public String getString(Object key){
		return getString(key, null);
	}
	
	public String getString(Object key, String def){
		Object val = get(key, def);
		return convert(val, String.class, def);
	}
	
	public Boolean getBoolean(Object key){
		Object val = get(key);
		return convert(val, Boolean.class, false);
	}
	
	public Boolean getBoolean(Object key, Boolean def){
		Object val = get(key, def);
		return convert(val, Boolean.class, def);
	}
	
	public Long getLong(String key){
		return this.getLong(key, null);
	}
	
	public Long getLong(String key, Long def){
		Object val = get(key);
		return convert(val, Long.class, def);
	}
	
	public Double getDouble(String key, Double def){
		Object val = get(key);
		return convert(val, Double.class, def);
	}
	
	public Float getFloat(String key, Float def){
		Object val = get(key);
		return convert(val, Float.class, def);
	}
	
	protected <T> T convert(Object val, Class<T> toType, T def){
		return MyUtils.simpleConvert(val, toType, def);
	}
	
	public Integer getInteger(String key){
		return getInteger(key, null);
	}
	
	public Integer getInteger(String key, Integer def){
		Object val = get(key);
		return convert(val, Integer.class, def);
	}
	
	public Object[] toArray(){
		return CUtils.toList(this).toArray();
	}
}
