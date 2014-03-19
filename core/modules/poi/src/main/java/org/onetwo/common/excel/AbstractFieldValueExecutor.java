package org.onetwo.common.excel;

import org.onetwo.common.excel.DefaultRowProcessor.CellContext;

abstract public class AbstractFieldValueExecutor implements FieldValueExecutor {

	@Override
	public boolean apply(CellContext cellContext, ExecutorModel executorModel) {
		return true;
	}

	@Override
	public Object execute(CellContext cellContext, ExecutorModel executorModel, Object preResult) {
		return doExecute(cellContext, executorModel, preResult);
	}
	

	abstract public Object doExecute(CellContext cellContext, ExecutorModel executorModel, Object preResult);
	

}
