package org.onetwo.common.db.generator.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

public class TableMeta {

	private String name;
	private ColumnMeta primaryKey;

	private Map<String, ColumnMeta> columnMap = new LinkedHashMap<String, ColumnMeta>();
	
	private String comment;
	
	private String stripPrefix;

	
	public TableMeta(String name, String comment){
//		this.entry = entry;
		Assert.hasText(name, "table name must has text");
		this.name = name;
		this.comment = comment;
	}
	
	public String getShortName(){
		return tableNameStripStart(stripPrefix);
	}
	
	public String getTableName(){
		return StringUtils.toClassName(getShortName());
	}
	
	public String getPropertyName(){
		return StringUtils.toPropertyName(getShortName());
	}

	
	public String getComment() {
		return comment;
	}


	public String getName() {
		return this.name;
	}
	
	public String tableNameStripStart(String stripChars){
		if(StringUtils.isBlank(stripChars))
			return name;
		return StringUtils.stripStart(name.toLowerCase(), stripChars.toLowerCase());
	}

	public Map<String, ColumnMeta> getColumnMap() {
		return ImmutableMap.copyOf(columnMap);
	}

	public Collection<ColumnMeta> getColumns() {
		return columnMap.values();
	}

	public Collection<ColumnMeta> getColumnCollection() {
		return new ArrayList<ColumnMeta>(columnMap.values());
	}

	public Collection<ColumnMeta> getSelectableColumns() {
		List<ColumnMeta> cols = LangUtils.newArrayList();
		for(ColumnMeta col : this.columnMap.values()){
			cols.add(col);
		}
		return cols;
	}

	public ColumnMeta getColumn(String name) {
		return columnMap.get(name);
	}

	public void setColumns(Map<String, ColumnMeta> columns) {
		this.columnMap = columns;
	}

	public TableMeta addColumn(ColumnMeta column) {
		column.setTable(this);
		if(column.isPrimaryKey()){
			column.setPrimaryKey(true);
		}
		this.columnMap.put(column.getName(), column);
		return this;
	}

	public ColumnMeta getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(ColumnMeta primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public List<String> getComments() {
		return Splitter.on('\n').trimResults().omitEmptyStrings().splitToList(comment);
	}

	public String getStripPrefix() {
		return stripPrefix;
	}

	public void setStripPrefix(String stripPrefix) {
		this.stripPrefix = stripPrefix;
	}

	@Override
	public String toString() {
		return "TableMeta [name=" + name + ", primaryKey=" + primaryKey
				+ ", columns=" + columnMap + ", comment=" + comment + "]";
	}

}
