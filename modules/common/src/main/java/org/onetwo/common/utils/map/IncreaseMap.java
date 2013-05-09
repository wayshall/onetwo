package org.onetwo.common.utils.map;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.list.L;

@SuppressWarnings({ "unchecked", "serial" })
public class IncreaseMap extends BaseMap{
	
	public IncreaseMap(){super();};
	
	public IncreaseMap(Map map){
		super(map);
	}
	
	public Object put(Object key, Object value){
		return this.increasePut(key, value);
	}
	
	public Object increasePut(Object key, Object value){
		Object e = get(key);
		if(containsKey(key)){
			List<Object> results = L.IfNullCreate(e);
			results.add(value);
			return super.put(key, results);
		}else{
			return super.put(key, value);
		}
	}
	
	 public boolean containsValue(Object value) {
		for(Object key : this.keySet()){
			if(getValueList(key).contains(value))
				return true;
		}
		return false;
	 }
	
	public List getValueList(Object key){
		Object value = get(key);
		return L.tolist(value, false, Collections.emptyList());
	}
	
}
