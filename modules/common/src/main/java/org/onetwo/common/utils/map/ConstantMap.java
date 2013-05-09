package org.onetwo.common.utils.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.onetwo.common.utils.map.KVEntry.ConstantKVEntry;

/********
 * 不可通过put, clear, remove接口修改的map
 * @Deprecated
 *
 * @param <K>
 * @param <V>
 */

public class ConstantMap<K, V> extends LinkedHashMap<K, V> implements Map<K, V>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2506959171227971363L;

	public static <K, V> ConstantMap<K, V> create() {
		return new ConstantMap<K, V>();
	}

	public static <K, V> ConstantMap<K, V> create(Class<K> kc, Class<V> vc) {
		return new ConstantMap<K, V>();
	}
	
	private boolean freezing;
	
	public ConstantMap(){
	}

	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException("ConstantMap unsupported put!");
	}

	public V remove(Object key) {
		throw new UnsupportedOperationException("ConstantMap unsupported remove!");
	}

	public void clear() {
		throw new UnsupportedOperationException("ConstantMap unsupported clear!");
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException("ConstantMap unsupported putAll");
	}

	public KVEntry<K, V> constant(K k, V v) {
		if(freezing){
			throw new UnsupportedOperationException("can not add constant...");
		}
		super.put(k, v);
		return new ConstantKVEntry<K, V>(k, v);
	}

	public ConstantMap<K, V> addConst(K k, V v) {
		constant(k, v);
		return this;
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
	
	public ConstantMap<K, V> freezing(){
		this.freezing = true;
		return this;
	}
}
