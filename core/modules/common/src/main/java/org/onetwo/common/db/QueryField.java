package org.onetwo.common.db;

public interface QueryField {
	public static final char SPLIT_SYMBOL = ':';
	
	public void init(ExtQuery extQuery, Object value);
	
	public String getActualFieldName();
	
	public String getOperator();
	

	public ExtQuery getExtQuery();

	public Object getValue();

}
