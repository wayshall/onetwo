package org.onetwo.common.utils.map;

import java.util.HashMap;
import java.util.Map;

public class TableMap<RK, CK, V> {

	private Map<RK, Map<CK, V>> rowMap = new HashMap<>();
	private Map<CK, Map<RK, V>> columnMap = new HashMap<>();

	public int rowSize(){
		return rowMap.size();
	}
	public int size(){
		int size = 0;
		for(Map<CK, V> row: rowMap.values()){
			size += row.size();
		}
		return size;
	}

	public boolean containsRow(RK rowKey){
		return rowMap.containsKey(rowKey);
	}
	public boolean containsColumn(CK columnKey){
		return columnMap.containsKey(columnKey);
	}
	public boolean contains(RK rowKey, CK columnKey){
		return rowMap.containsKey(rowKey) && columnMap.containsKey(columnKey);
	}
	public V get(RK rowKey, CK columKey){
		return get(rowKey, columKey, null);
	}
	public V getByRC(RK rowKey, CK columKey){
		return get(rowKey, columKey, null);
	}
	public V getByCR(CK columKey, RK rowKey){
		return get(rowKey, columKey, null);
	}
	
	public V get(RK rowKey, CK columKey, V defaultValue){
		Map<CK, V> row = getRow(rowKey);
		if(row==null)
			return defaultValue;
		return row.containsKey(columKey)?row.get(columKey):defaultValue;
	}
	public Map<CK, V> getRow(RK rowKey){
		return rowMap.get(rowKey);
	}
	public Map<RK, V> getColumn(RK columnKey){
		return columnMap.get(columnKey);
	}

	public V put(RK rowKey, CK columKey, V value){
		putIntoRow(rowKey, columKey, value);
		V v = putIntoColumn(rowKey, columKey, value);
		return v;
	}
	
	public V putIntoRow(RK rowKey, CK columKey, V value){
		Map<CK, V> row = rowMap.get(rowKey);
		if(row==null){
			row = new HashMap<>();
			this.rowMap.put(rowKey, row);
		}
		return row.put(columKey, value);
	}
	
	public V putIntoColumn(RK rowKey, CK columKey, V value){
		Map<RK, V> columns = columnMap.get(columKey);
		if(columns==null){
			columns = new HashMap<>();
			this.columnMap.put(columKey, columns);
		}
		return columns.put(rowKey, value);
	}

}
