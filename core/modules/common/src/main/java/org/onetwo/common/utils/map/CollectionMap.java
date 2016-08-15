package org.onetwo.common.utils.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.onetwo.common.utils.LangUtils;

public class CollectionMap<K, V> implements Map<K, Collection<V>>{
	
	public static <K, V> CollectionMap<K, V> newListMap(){
		return new CollectionMap<K, V>(new HashMap<K, Collection<V>>());
	}
	
	public static <K, V> CollectionMap<K, V> newListMap(Supplier<Collection<V>> collectionCreator){
		return new CollectionMap<K, V>(new HashMap<K, Collection<V>>(), collectionCreator);
	}
	
	public static <K, V> CollectionMap<K, V> newListMap(int size){
		return new CollectionMap<K, V>(new HashMap<K, Collection<V>>(size));
	}
	
	public static <K, V> CollectionMap<K, V> newLinkedListMap(){
		return new CollectionMap<K, V>(new LinkedHashMap<K, Collection<V>>());
	}
	
	public static <K, V> CollectionMap<K, V> newLinkedListMap(Supplier<Collection<V>> collectionCreator){
		return new CollectionMap<K, V>(new LinkedHashMap<K, Collection<V>>(), collectionCreator);
	}
	
	public static <K, V> CollectionMap<K, V> newListMap(Map<K, Collection<V>> map){
		return new CollectionMap<K, V>(map);
	}

	private Map<K, Collection<V>> map;
	private Supplier<Collection<V>> collectionCreator = ()->new HashSet<V>();
	
	protected CollectionMap(){
		this(new LinkedHashMap<K, Collection<V>>());
	}
	
	
	protected CollectionMap(Map<K, Collection<V>> map) {
		super();
		this.map = map;
	}


	public CollectionMap(Map<K, Collection<V>> map, Supplier<Collection<V>> collectionCreator) {
		super();
		this.map = map;
		this.collectionCreator = collectionCreator;
	}

	public boolean putElement(K key, V value){
		Collection<V> list = get(key);
		if(list==null){
			list = createCollection();
			map.put(key, list);
		}
		return list.add(value);
	}
	
	private Collection<V> createCollection(){
		return collectionCreator.get();
	}
	public V removeFirst(K key){
		Collection<V> list = get(key);
		if(LangUtils.isEmpty(list)){
			return null;
		}
		Iterator<V> it = list.iterator();
		V v = it.next();
		it.remove();
		/*if(list.isEmpty()){
			map.remove(key);
		}*/
		return v;
	}

	public Collection<V> putElements(K key, V... values){
		Collection<V> list = get(key);
		if(list==null){
			list = createCollection();
			map.put(key, list);
		}
		for(V v : values)
			list.add(v);
		return list;
	}


	public int size() {
		return map.size();
	}


	public boolean isEmpty() {
		return map.isEmpty();
	}


	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}


	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}


	public Collection<V> get(Object key) {
		return map.get(key);
	}

	public V getFirstValue(Object key) {
		Collection<V> list = map.get(key);
		if(LangUtils.isEmpty(list))
			return null;
		return list.iterator().next();
	}


	public Collection<V> put(K key, Collection<V> value) {
		return map.put(key, value);
	}


	public Collection<V> remove(Object key) {
		return map.remove(key);
	}


	public void putAll(Map<? extends K, ? extends Collection<V>> m) {
		map.putAll(m);
	}


	public void clear() {
		map.clear();
	}


	public Set<K> keySet() {
		return map.keySet();
	}


	public Collection<Collection<V>> values() {
		return map.values();
	}


	public Set<java.util.Map.Entry<K, Collection<V>>> entrySet() {
		return map.entrySet();
	}


	public boolean equals(Object o) {
		return map.equals(o);
	}


	public int hashCode() {
		return map.hashCode();
	}
	

    public String toString() {
    	StringBuilder str = new StringBuilder("{");
    	int index = 0;
    	for(Map.Entry<K, Collection<V>> entry : (Set<Map.Entry<K, Collection<V>>>)entrySet()){
    		if(index!=0)
    			str.append(", ");
			str.append(entry.getKey()).append("=").append(entry.getValue());
			index++;
		}
    	str.append("}");
    	return str.toString();
    }
	
}
