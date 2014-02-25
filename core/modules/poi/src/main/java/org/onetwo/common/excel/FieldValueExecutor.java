package org.onetwo.common.excel;

import org.onetwo.common.interfaces.excel.ExcelValueParser;

public interface FieldValueExecutor {
	
	public boolean apply(FieldModel field, Object fieldVallue);
	
	public Object execute(FieldModel field, ExecutorModel executorModel, ExcelValueParser parser, Object fieldVallue, Object preResult);

}
