package org.onetwo.common.db;

public interface QueryField {
	public static final char SPLIT_SYMBOL = ':';
	
	public void init(ExtQueryInner extQuery, Object value);
	
	public String getActualFieldName();
	
	public String getOperator();
	

	public ExtQueryInner getExtQuery();

	public Object getValue();

}
