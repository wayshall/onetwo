package org.onetwo.common.db.wheel;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class ColumnInfo extends Propertable {
	
	private TableInfo table;
	
	protected String name;
	protected int sqlType; 
	protected boolean primaryKey;
	
	private String comment;
	
	public ColumnInfo(){
	}
	
	public ColumnInfo(TableInfo tableInfo, String name) {
		this.table = tableInfo;
		setName(name);
	}
	
	public ColumnInfo(String name) {
		setName(name);
	}

	public ColumnInfo(String name, int sqlType) {
		setName(name);
		this.sqlType = sqlType;
		this.javaType = SqlTypeFactory.getJavaType(sqlType); 
	}

	public TableInfo getTable(){
		return table;
	}

	public void setTable(TableInfo table){
		this.table = table;
	}
	public void setName(String name) {
		this.name = name;
		this.javaName = StringUtils.toPropertyName(name);
	}

	public String getName() {
		return name;
	}

	public int getSqlType() {
		return sqlType;
	}
	
	public boolean isDateType(){
		return getJavaType()==Date.class || getJavaType()==Time.class || getJavaType()==Timestamp.class;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
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
		final ColumnInfo other = (ColumnInfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public String toString(){
		return LangUtils.toString(false, "name:${0}, javaName:${1}", false, name, javaName);
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getNameWithAlias(){
		return withAlias(".", getName());
	}
	
	public String getJavaNameWithAlias(){
		return withAlias("", getJavaName());
	}
	
	public String getNamedPlaceHolder(){
		return ":" + getJavaName();
	}
	
	public String getNamedPlaceHolderWithAlias(){
		return ":" + getJavaNameWithAlias();
	}
	
	public String getPlaceHolder(){
		return "?";
	}
	
	public String withAlias(String sep, String name){
		return LangUtils.append(table.getAlias(), sep, name);
	}
	
	/*public String getPlaceHolderForWhere(){
		return getPlaceHolderForWhere("?");
	}
	
	
	public String getPlaceHolderForWhere(String placeHolder){
		return getPlaceHolderForWhere(null, placeHolder);
	}*/

}
