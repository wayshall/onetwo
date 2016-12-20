package org.onetwo.ext.poi.excel.generator;

import org.onetwo.ext.poi.excel.data.CellContextData;


public interface FieldValueExecutor {
	
	public boolean apply(CellContextData cellContext, ExecutorModel executorModel);
	
	public Object execute(CellContextData cellContext, ExecutorModel executorModel, Object preResult);

}
