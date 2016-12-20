package org.onetwo.ext.poi.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.ext.poi.excel.reader.SheetRowMapper;

public class MapDataExtractor<K, V> extends AbstractTableDataExtractor<V, Map<K, V>, Sheet>{

	private String keyProperty;

	public MapDataExtractor(SheetRowMapper<V> mapper) {
		this(mapper, "id");
	}
	public MapDataExtractor(SheetRowMapper<V> mapper, String keyProperty) {
		super(mapper);
		this.keyProperty = keyProperty;
	}

	@Override
	protected Map<K, V> createDataContainer(int size) {
		return new HashMap<>(size);
	}

	@Override
	protected void addValueToContainer(Map<K, V> container, V value) {
		container.put(getKey(value), value);
	}

	@SuppressWarnings("unchecked")
	protected K getKey(V value){
		Object key = Intro.wrap(value.getClass()).getPropertyValue(value, keyProperty);
		if(key==null){
			throw new RuntimeException("no key value found for property: " + keyProperty);
		}
		return (K)key;
	}

}
