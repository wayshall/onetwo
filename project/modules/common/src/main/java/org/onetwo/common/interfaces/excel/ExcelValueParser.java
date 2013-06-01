package org.onetwo.common.interfaces.excel;

import java.util.Map;

public interface ExcelValueParser {
	public Map<String, Object> getContext();
	public Object parseValue(String expr, Object rootForExpr, Object objForSymbol);
	
	public int parseIntValue(String expr, Object root);
	
	/*public boolean isSymbol(String value);
	public String getSymbol(String value);
	public Object parseSymbol(String symbol, Object obj);*/
}
