package org.onetwo.common.excel;

import org.onetwo.common.excel.data.CellContextData;


public interface FieldValueExecutor {
	
	public boolean apply(CellContextData cellContext, ExecutorModel executorModel);
	
	public Object execute(CellContextData cellContext, ExecutorModel executorModel, Object preResult);

}
