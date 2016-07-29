package org.onetwo.common.jfishdbm.mapping;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.FetchType;

import org.onetwo.common.utils.StringUtils;

abstract public class BaseColumnInfo extends Propertable{

	public static final String QMARK = "?";
	
	private String name;

	private boolean insertable = true;
	private boolean updatable = true;

	protected boolean primaryKey;
	protected boolean referencedKey;
	
	protected FetchType fetchType;
	
	private String comment;
	
	public BaseColumnInfo(){
	}

	public BaseColumnInfo(String name) {
		setName(name);
	}

	abstract public int getSqlType();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.javaName = StringUtils.toPropertyName(name);
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
	
	public String withAlias(String sep, String name){
		return name;
	}
	
	public String getPlaceHolder(){
		return QMARK;
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public String getComment() {
		return comment;
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

	public FetchType getFetchType() {
		return fetchType;
	}

	public void setFetchType(FetchType fetchType) {
		this.fetchType = fetchType;
	}
	
	public boolean isLazy(){
		return FetchType.LAZY.equals(this.fetchType);
	}
	
}
