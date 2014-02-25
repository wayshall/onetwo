package org.onetwo.common.excel;


public class SumFieldValueExecutor extends AbstractFieldValueExecutor {


	@Override
	public boolean apply(FieldModel field, Object fieldVallue) {
		try {
			Double.parseDouble(fieldVallue.toString());
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public Object doExecute(FieldModel field, Object fieldVallue, Object preResult) {
		Double total = null;
		if(preResult==null)
			total = 0D;
		else
			total = (Double)preResult;
		return total + Double.parseDouble(fieldVallue.toString());
	}

	
	

}
