package org.onetwo.common.db.builder;

import org.onetwo.common.db.sqlext.ExtQueryInner;

public interface QueryField {
	public static final char SPLIT_SYMBOL = ':';
	
	public void init(ExtQueryInner extQuery, Object value);
	
	public String getActualFieldName();
	
	public String getOperator();
	

	public ExtQueryInner getExtQuery();

	public Object getValue();

	public String getFieldName();
}
