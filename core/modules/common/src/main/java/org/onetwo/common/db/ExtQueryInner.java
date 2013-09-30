package org.onetwo.common.db;

public interface ExtQueryInner extends ExtQuery {
	
	public String getFieldName(String f);
	
	public SQLFunctionManager getSqlFunctionManager();
}
