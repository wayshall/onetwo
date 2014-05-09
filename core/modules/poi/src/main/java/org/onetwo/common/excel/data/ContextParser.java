package org.onetwo.common.excel.data;


public interface ContextParser {

//	public Map<String, Object> getContext(ConxtScope scope);
	
	public Object parseValue(String expr);
	public int parseIntValue(String expr);
	public void initData();
	
}
