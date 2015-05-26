package org.onetwo.common.jfishdbm.mapping;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("rawtypes")
public class PrimaryKey extends BaseColumnInfo {

	protected TableInfo table;
	private List<ColumnInfo> columns = new ArrayList<ColumnInfo>();

	public PrimaryKey() {
	}
	
	public PrimaryKey(String name) {
		setName(name);
	}
	
	public Class<?> getJavaType(){
		Class type = null;
		if(this.columns.size()==1){
			type = this.columns.get(0).getJavaType();
		}else{
			type = super.getJavaType()==null?Long.class:super.getJavaType();
		}
		return type;
	}
	
	public boolean isGeneratorId(){
		return getJavaType()==Long.class;
	}

	public List<ColumnInfo> getColumns() {
		return columns;
	}

	public ColumnInfo getColumn(String name) {
		for(ColumnInfo col : this.columns){
			if(col.getName().equalsIgnoreCase(name))
				return col;
		}
		return null;
	}

	public void setColumns(List<ColumnInfo> columns) {
		this.columns = columns;
	}

	public PrimaryKey addColumn(ColumnInfo column) {
		column.setPrimaryKey(true);
		if(StringUtils.isBlank(getName()))
			setName(column.getName());
		this.columns.add(column);
		return this;
	}
	
	public boolean isCompositeId(){
		return this.columns!=null && this.columns.size()>1;
	}
	
	public ColumnInfo getFirstColumn(){
		return this.columns.get(0);
	}

	public TableInfo getTable() {
		return table;
	}

	public void setTable(TableInfo table) {
		this.table = table;
	}
	
}
