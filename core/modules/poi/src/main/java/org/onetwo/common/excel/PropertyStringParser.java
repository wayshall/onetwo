package org.onetwo.common.excel;

import java.util.Map;

public interface PropertyStringParser {

	public Map<Integer, Short> parseColumnwidth(String propertyValue);
	
	public Map<String, String> parseStyle(String styleString);
}
