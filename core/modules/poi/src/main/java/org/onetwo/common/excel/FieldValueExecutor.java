package org.onetwo.common.excel;


public interface FieldValueExecutor {
	
	public boolean apply(CellContext cellContext, ExecutorModel executorModel);
	
	public Object execute(CellContext cellContext, ExecutorModel executorModel, Object preResult);

}
