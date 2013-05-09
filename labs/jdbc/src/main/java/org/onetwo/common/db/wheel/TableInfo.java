package org.onetwo.common.db.wheel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class TableInfo {

	private String name;
	private String alias = "this_";
	private String seqName;
	
	private String prefix; 

	private Class<?> entityClass;

	private PrimaryKey primaryKey;

	private Map<String, ColumnInfo> columns = new LinkedHashMap<String, ColumnInfo>();
	
	private String comment;

	private boolean dbCreatePrimaryKey = true;
	private boolean dynamic;
	
	public TableInfo(String name) {
		this(name, "");
	}

	public TableInfo(String name, String prefix) {
		this.name = name;
		setPrefix(prefix);
	}
	
	public TableInfo(Class<?> entityClass, String name){
		this.entityClass = entityClass;
		if(Map.class.isAssignableFrom(entityClass)){
			this.setDynamic(true);
		}
		if(StringUtils.isBlank(name))
			this.name = StringUtils.toClassName(this.entityClass.getSimpleName());
		else
			this.name = name;
	}

	public boolean isDbCreatePrimaryKey(){
		return dbCreatePrimaryKey;
	}
	public void setDbCreatePrimaryKey(boolean dbCreatePrimaryKey){
		this.dbCreatePrimaryKey = dbCreatePrimaryKey;
	}
	
	public String getName() {
		return this.name;
	}
	public String getNamedPlaceHolder(){
		return ":" + getName();
	}
	

	public TableInfo id(String name){
		return id(name, false);
	}

	public TableInfo id(String name, boolean createIfNull){
		ColumnInfo col = this.getColumn(name);
		if(col==null){
			if(createIfNull){
				col = new ColumnInfo(name);
				addColumn(col);
			}else{
				LangUtils.throwBaseException("can not find the column: " + name);
			}
		}
		PrimaryKey pk = this.getPrimaryKey();
		if(pk==null){
			pk = new PrimaryKey();
			pk.setTable(this);
		}
		pk.addColumn(col);
		setPrimaryKey(pk);
		return this;
	}

	public Map<String, ColumnInfo> getColumns() {
		return columns;
	}

	public List<ColumnInfo> getColumnCollection() {
		return new ArrayList<ColumnInfo>(columns.values());
	}

	public ColumnInfo getColumn(String name) {
		return columns.get(name);
	}

	public void setColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}

	public TableInfo addColumn(ColumnInfo column) {
		column.setTable(this);
		this.columns.put(column.getName(), column);
		return this;
	}

	public TableInfo addColumn(String name, int sqlType) {
		ColumnInfo column = new ColumnInfo(name, sqlType);
		return addColumn(column);
	}

	public TableInfo column(String name) {
		return column(name, null);
	}

	public TableInfo column(String name, String propName) {
		ColumnInfo column = new ColumnInfo(name);
		if(StringUtils.isNotBlank(propName))
			column.setJavaName(propName);
		return addColumn(column);
	}

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getClassName() {
		if(entityClass!=null)
			return entityClass.getSimpleName();
		return StringUtils.toJavaName(this.name.substring(prefix.length()), true);
	}

	public String getEntityName() {
		return this.entityClass.getName();
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
		if(StringUtils.isNotBlank(seqName))
			setDbCreatePrimaryKey(false);
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

}
