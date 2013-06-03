package org.onetwo.common.fish.orm;

import java.util.HashMap;

public class DataHolder<K, V> extends HashMap<K, V>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6167731907137734460L;

	public <T> T getFromDataHolder(Class<T> clazz){
		for(Object obj : this.values()){
			if(clazz.isInstance(obj))
				return (T)obj;
		}
		return null;
	}
}
