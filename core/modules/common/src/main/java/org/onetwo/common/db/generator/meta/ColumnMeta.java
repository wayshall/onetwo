package org.onetwo.common.db.generator.meta;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.onetwo.common.db.generator.DBUtils;
import org.onetwo.common.db.generator.mapping.ColumnMapping;
import org.onetwo.common.utils.StringUtils;

import com.google.common.base.Splitter;

public class ColumnMeta {

	protected TableMeta table;
	private String name;
	protected int sqlType = DBUtils.TYPE_UNKNOW;

	protected boolean primaryKey;
	protected boolean referencedKey;
	
	private String comment;
	

	protected String javaName;
//	protected Class<?> javaType;
	
	protected ColumnMapping mapping;
	
	public ColumnMeta(TableMeta tableInfo, String name, int sqlType, ColumnMapping mapping) {
		setName(name);
		setSqlType(sqlType);
//		setJavaType(javaType);
		this.mapping = mapping;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.javaName = StringUtils.toPropertyName(name);
	}
	
	public String getComment() {
		return comment;
	}
	
	public List<String> getComments() {
		return Splitter.on('\n').trimResults().omitEmptyStrings().splitToList(comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isReferencedKey() {
		return referencedKey;
	}

	public void setReferencedKey(boolean referencedKey) {
		this.referencedKey = referencedKey;
	}
	
	public boolean isDateType(){
		return getJavaType()==Date.class || getJavaType()==Time.class || getJavaType()==Timestamp.class;
	}

	public TableMeta getTable() {
		return table;
	}

	public void setTable(TableMeta table) {
		this.table = table;
	}

	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

	public String getJavaName() {
		return javaName;
	}

	public Class<?> getJavaType() {
		return this.mapping.getJavaType(); 
	}

	public void setJavaName(String javaName) {
		this.javaName = javaName;
	}

	public String getReadMethodName() {
		String prefix = "get";
		String name = this.getJavaName();
		if(getJavaType()==Boolean.class){
			prefix = "is";
			if(name.startsWith(prefix))
				name = name.substring(prefix.length());
		}
		name = name.substring(0, 1).toUpperCase()+this.getJavaName().substring(1);
		return prefix+name;
	}

	public String getWriteMethodName() {
		String prefix = "set";
		String name = this.getJavaName().substring(0, 1).toUpperCase()+this.getJavaName().substring(1);
		return prefix+name;
	}

	@Override
	public String toString() {
		return "ColumnMeta [name=" + name + ", comment=" + comment + "]";
	}

	public ColumnMapping getMapping() {
		return mapping;
	}

	public void setMapping(ColumnMapping mapping) {
		this.mapping = mapping;
	}

}
