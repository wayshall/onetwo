package org.onetwo.common.excel;

import org.onetwo.common.interfaces.excel.ExcelValueParser;

abstract public class AbstractFieldValueExecutor implements FieldValueExecutor {

	@Override
	public boolean apply(FieldModel field, Object fieldVallue) {
		return true;
	}

	@Override
	public Object execute(FieldModel field, ExecutorModel executorModel, ExcelValueParser parser, Object fieldVallue, Object preResult) {
		return doExecute(field, fieldVallue, preResult);
	}
	

	abstract public Object doExecute(FieldModel field, Object fieldVallue, Object preResult);
	

}
