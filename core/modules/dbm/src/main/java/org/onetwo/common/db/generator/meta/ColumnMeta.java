package org.onetwo.common.db.generator.meta;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.generator.mapping.ColumnMapping;
import org.onetwo.common.db.generator.utils.DbGeneratorUtills;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.StringUtils;

import com.google.common.base.Splitter;

public class ColumnMeta {

	protected TableMeta table;
	private String name;
//	protected int sqlType = DBUtils.TYPE_UNKNOW;

	protected boolean primaryKey;
//	protected boolean referencedKey;
	
	protected boolean nullable;
	private String comment;
	private Map<String, String> commentsInfo = Collections.emptyMap();
	protected int columnSize;
	

	protected String javaName;
//	protected Class<?> javaType;
	
	protected ColumnMapping mapping;
	
	public ColumnMeta(TableMeta tableInfo, String name, ColumnMapping mapping) {
		setName(name);
//		setSqlType(sqlType);
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
		return CUtils.iterableToList(Splitter.on('\n').trimResults().omitEmptyStrings().split(comment));
	}
	
	public Map<String, String> getCommentsInfo() {
		return commentsInfo;
	}

	public String getIndexComment(int index) {
		List<String> comments = getComments();
		if(comments.size()<index){
			return comments.get(index);
		}else{
			return "";
		}
	}

	public void setComment(String comment) {
		this.comment = comment;
		this.commentsInfo = DbGeneratorUtills.parse(comment);
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	/*public boolean isReferencedKey() {
		return referencedKey;
	}

	public void setReferencedKey(boolean referencedKey) {
		this.referencedKey = referencedKey;
	}*/
	
	public boolean isDateType(){
		return this.mapping.isDateType();
	}

	public TableMeta getTable() {
		return table;
	}

	public void setTable(TableMeta table) {
		this.table = table;
	}

	public int getSqlType() {
		return mapping.getSqlType();
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
	
	public String getPropertyName(){
		return getJavaName();
	}
	
	public String getCapitalizePropertyName(){
		return StringUtils.capitalize(getPropertyName());
	}

	public String getReadMethodName() {
		return getReadMethodName(true);
	}

	public String getReadMethodName(boolean convertBooleanMethod) {
		String prefix = "get";
		String name = this.getJavaName();
		if(getJavaType()==Boolean.class && convertBooleanMethod){
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

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

}
