package org.onetwo.common.excel;

import org.onetwo.common.excel.data.CellContextData;


abstract public class AbstractFieldValueExecutor implements FieldValueExecutor {

	@Override
	public boolean apply(CellContextData cellContext, ExecutorModel executorModel) {
		return true;
	}

	@Override
	public Object execute(CellContextData cellContext, ExecutorModel executorModel, Object preResult) {
		return doExecute(cellContext, executorModel, preResult);
	}
	

	abstract public Object doExecute(CellContextData cellContext, ExecutorModel executorModel, Object preResult);
	

}
