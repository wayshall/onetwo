package org.onetwo.common.excel;

import java.util.Map;

@SuppressWarnings("unchecked")
public interface FieldExecutor {
	
	public void process(Map context, FieldModel field, Object dataSourceValue);

}
