package org.onetwo.common.excel;

import org.onetwo.common.excel.DefaultRowProcessor.CellContext;
import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;


public class SumFieldValueExecutor extends AbstractFieldValueExecutor {

	private static final Logger logger = MyLoggerFactory.getLogger(SumFieldValueExecutor.class);

	@Override
	public boolean apply(CellContext cellContext) {
		if(!cellContext.getField().getRow().isIterator())
			return false;
		try {
			Double.parseDouble(cellContext.getFieldValue().toString());
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	@Override
	public Object doExecute(CellContext cellContext, Object preResult) {
		Double total = null;
		if(preResult==null)
			total = 0D;
		else
			total = (Double)preResult;
		total = total + Double.parseDouble(cellContext.getFieldValue().toString());
		logger.info(cellContext.getField().getLabel()+" preResult: "+preResult+" now: "+cellContext.getFieldValue().toString()+" total: "+total);
		return total;
	}

	
	

}
