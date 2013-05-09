package org.onetwo.common.utils.map;

@SuppressWarnings({ "unchecked", "serial" })
public class NonCaseMap<K, V> extends BaseMap<K, V>{

	@Override
	public V get(Object key) {
		key = convertKey(key);
		return super.get(key);
	}
	
	protected Object convertKey(Object key){
		if(key instanceof String){
			key = key.toString().toLowerCase();
		}
		return key;
	}

	@Override
	public V put(K key, V value) {
		key = (K)convertKey(key);
		return super.put(key, value);
	}

}
