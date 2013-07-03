package org.onetwo.common.utils.map;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

public class KVEntry<K, V> implements Map.Entry<K, V> {
	
	public static class ConstantKVEntry<K, V> extends KVEntry<K, V> {

		public ConstantKVEntry(K key, V val) {
			super(key, val);
		}

		@Override
		public void setKey(K key) {
			throw new UnsupportedOperationException("read only!");
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException("read only!");
		}
		
	}
	
	public static <K, V> KVEntry<K, V> create(Entry<K, V> entry){
		return create(entry.getKey(), entry.getValue(), null);
	}
	
	public static <K, V> KVEntry<K, V> create(K k, V v){
		return create(k, v, null);
	}
	
	public static <K, V> KVEntry<K, V> create(K k, V v, Map<K, V> map){
		KVEntry<K, V> entry = new KVEntry<K, V>(k, v);
		if(map!=null)
			map.put(k, v);
		return entry;
	}
	
	public static <K, V> KVEntry<K, V> constEntry(K k, V v, Map<K, V> map){
		KVEntry<K, V> entry = new ConstantKVEntry<K, V>(k, v);
		if(map!=null)
			map.put(k, v);
		return entry;
	}
	
	public static <K, V> Map<K, V> freezeMap(Map<K, V> map){
		return Collections.unmodifiableMap(map);
	}
	
	
	private K key;
	private V value;
	
	public KVEntry(K key, V val) {
		super();
		this.key = key;
		this.value = val;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public V setValue(V value) {
		V t = this.value;
		this.value = value;
		return t;
	}
}
