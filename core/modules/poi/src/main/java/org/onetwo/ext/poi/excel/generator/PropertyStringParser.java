package org.onetwo.ext.poi.excel.generator;

import java.util.Map;

public interface PropertyStringParser {

	public Map<Integer, Short> parseColumnwidth(String propertyValue);
	
	public Map<String, String> parseStyle(String styleString);
}
