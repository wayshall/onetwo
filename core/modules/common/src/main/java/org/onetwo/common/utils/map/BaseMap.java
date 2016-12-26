package org.onetwo.common.utils.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.convert.Types;
import org.onetwo.common.utils.CUtils;

/****
 * 提供类型转换的get方法
 * @author way
 *
 * @param <K>
 * @param <V>
 */
public class BaseMap<K, V> implements Map<K, V>{
	
	final private Map<K, V> delegateMap;
	
	public BaseMap(){
		this.delegateMap = new HashMap<>();
	}
	
	public BaseMap(int initialCapacity) {
		this.delegateMap = new HashMap<>(initialCapacity);
	}
	
	public BaseMap(Map<K, V> map){
		delegateMap = map;
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
//		return MyUtils.simpleConvert(val, toType, def);
		return Types.convertValue(val, toType, def);
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

	public int size() {
		return delegateMap.size();
	}

	public boolean isEmpty() {
		return delegateMap.isEmpty();
	}

	public boolean containsKey(Object key) {
		return delegateMap.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return delegateMap.containsValue(value);
	}

	public V get(Object key) {
		return delegateMap.get(key);
	}

	public V put(K key, V value) {
		return delegateMap.put(key, value);
	}

	public V remove(Object key) {
		return delegateMap.remove(key);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		delegateMap.putAll(m);
	}

	public void clear() {
		delegateMap.clear();
	}

	public Set<K> keySet() {
		return delegateMap.keySet();
	}

	public Collection<V> values() {
		return delegateMap.values();
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return delegateMap.entrySet();
	}

	public boolean equals(Object o) {
		return delegateMap.equals(o);
	}

	public int hashCode() {
		return delegateMap.hashCode();
	}
	
}
