package org.onetwo.common.db;

import java.util.Set;

public interface ExtQueryInner extends ExtQuery {
	
	public String getFieldName(String f);
	
	public SQLFunctionManager getSqlFunctionManager();
	

	public boolean hasParameterField(String fieldName);
	public Set<String> getAllParameterFieldNames();
}
