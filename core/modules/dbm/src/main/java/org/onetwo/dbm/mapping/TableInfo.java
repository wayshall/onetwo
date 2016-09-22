package org.onetwo.dbm.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;

public class TableInfo {

	private String name;
	private String alias = "this_";
	private String seqName;
	
	private String prefix; 

//	private FishMappedEntry entry;

	private PrimaryKey primaryKey;

	private Map<String, BaseColumnInfo> columns = new LinkedHashMap<String, BaseColumnInfo>();
	
	private String comment;

//	private boolean dbCreatePrimaryKey = true;
	
	/*public TableInfo(String name) {
		this(name, "");
	}

	public TableInfo(String name, String prefix) {
		this.name = name;
		setPrefix(prefix);
	}*/
	
	public TableInfo(String name){
//		this.entry = entry;
		Assert.hasText(name, "table name must has text");
		this.name = name;
	}

	/*public boolean isDbCreatePrimaryKey(){
		return dbCreatePrimaryKey;
	}
	public void setDbCreatePrimaryKey(boolean dbCreatePrimaryKey){
		this.dbCreatePrimaryKey = dbCreatePrimaryKey;
	}*/
	
	public String getName() {
		return this.name;
	}
	public String getNamedPlaceHolder(){
		return ":" + getName();
	}

	public TableInfo id(String name){
		ColumnInfo col = (ColumnInfo)this.getColumn(name);
		if(col==null){
			LangUtils.throwBaseException("can not find the column: " + name);
		}
		return id(col);
	}

	public TableInfo id(ColumnInfo col){
		PrimaryKey pk = this.getPrimaryKey();
		if(pk==null){
			pk = new PrimaryKey();
			pk.setTable(this);
			setPrimaryKey(pk);
		}
		pk.addColumn(col);
		return this;
	}

	public Map<String, BaseColumnInfo> getColumns() {
		return columns;
	}

	public Collection<BaseColumnInfo> getColumnCollection() {
		return new ArrayList<BaseColumnInfo>(columns.values());
	}

	public Collection<BaseColumnInfo> getSelectableColumns() {
		List<BaseColumnInfo> cols = LangUtils.newArrayList();
		for(BaseColumnInfo col : this.columns.values()){
			if(col.isLazy())
				continue;
			cols.add(col);
		}
		return cols;
	}

	public BaseColumnInfo getColumn(String name) {
		return columns.get(name);
	}

	public void setColumns(Map<String, BaseColumnInfo> columns) {
		this.columns = columns;
	}

	public TableInfo addColumn(BaseColumnInfo column) {
		if(ColumnInfo.class.isInstance(column)){
			ColumnInfo dbcol = (ColumnInfo)column;
			dbcol.setTable(this);
			if(dbcol.isPrimaryKey())
				id(dbcol);
		}
		this.columns.put(column.getName(), column);
		return this;
	}
/*
	public TableInfo addColumn(String name, int sqlType) {
		ColumnInfo column = new ColumnInfo(name, sqlType);
		return addColumn(column);
	}

	public TableInfo column(String name) {
		return column(name, null);
	}*/

	/*public TableInfo column(String name, String propName) {
		ColumnInfo column = new ColumnInfo(name);
		if(StringUtils.isNotBlank(propName))
			column.setJavaName(propName);
		return addColumn(column);
	}*/

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}


	public String getPrefix() {
		return prefix;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getSeqName() {
		return seqName;
	}
	public void setSeqName(String seqName) {
		this.seqName = seqName;
		/*if(StringUtils.isNotBlank(seqName))
			setDbCreatePrimaryKey(false);*/
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableInfo other = (TableInfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
