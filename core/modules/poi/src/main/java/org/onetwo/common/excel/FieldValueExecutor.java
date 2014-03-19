package org.onetwo.common.excel;

import org.onetwo.common.excel.DefaultRowProcessor.CellContext;

public interface FieldValueExecutor {
	
	public boolean apply(CellContext cellContext);
	
	public Object execute(CellContext cellContext, ExecutorModel executorModel, Object preResult);

}
