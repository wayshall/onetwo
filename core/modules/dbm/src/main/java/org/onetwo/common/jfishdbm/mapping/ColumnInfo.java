package org.onetwo.common.jfishdbm.mapping;

import org.onetwo.common.jfishdbm.utils.DBUtils;
import org.onetwo.common.utils.LangUtils;

@SuppressWarnings("rawtypes")
public class ColumnInfo extends BaseColumnInfo {

	protected TableInfo table;
	protected int sqlType = DBUtils.TYPE_UNKNOW;
	
	public ColumnInfo(TableInfo tableInfo, String name, int sqlType) {
		super(name);
		this.table = tableInfo;
	}
	
	public Class<?> getJavaType() {
		if(javaType!=null){
			return javaType;
		}
		/*if(sqlType!=Integer.MIN_VALUE){
			javaType = SqlTypeFactory.getJavaType(sqlType); 
		}*/
		return javaType;
	}

	public void setJavaType(Class javaType) {
		this.javaType = javaType;
//		this.sqlType = SqlTypeFactory.getType(javaType); 
	}
	
	public int getSqlType() {
		return sqlType;
	}
	
	public String toString(){
		return LangUtils.toStringWith(false, "name:${0}, javaName:${1}", false, getName(), javaName);
	}
	
	public TableInfo getTable() {
		return table;
	}
	public void setTable(TableInfo table) {
		this.table = table;
	}
	public String withAlias(String sep, String name){
		if(table==null)
			return super.withAlias(sep, name);
		return LangUtils.append(table.getAlias(), sep, name);
	}
}
