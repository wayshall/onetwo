package org.onetwo.ext.poi.excel.data;

import java.util.Map;

public interface ExcelValueParser {
//	public void putVar(String name, Object value);
	public void putVar(String name, Object value);
	public Map<String, Object> getContext();
	public Object parseValue(String expr, Object root, Map<String, Object> context);
	public int parseIntValue(String expr, Object root);
//	public Object parseValue(String expr, Object rootForExpr, Object objForSymbol);
//	public Object parseValue(String expr);
	
//	public int parseIntValue(String expr, Object root);
	
	/*public boolean isSymbol(String value);
	public String getSymbol(String value);
	public Object parseSymbol(String symbol, Object obj);*/
}
