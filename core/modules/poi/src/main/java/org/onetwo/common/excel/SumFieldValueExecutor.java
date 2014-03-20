package org.onetwo.common.excel;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;


public class SumFieldValueExecutor extends AbstractFieldValueExecutor {

	public static final String SUM_VALUE_FIELD = "sumValueField";
	public static final String SUM_VALUE_CONDITION = "sumValueCondition";

	private static final Logger logger = MyLoggerFactory.getLogger(SumFieldValueExecutor.class);

	@Override
	public boolean apply(CellContext cellContext, ExecutorModel executorModel) {
		if(!cellContext.getFieldModel().getRow().isIterator())
			return false;
		
		String sumValueCondition = executorModel.getAttributes().get(SUM_VALUE_CONDITION);
		if(StringUtils.isNotBlank(sumValueCondition)){
			Boolean apply = (Boolean)cellContext.getParser().parseValue(sumValueCondition, cellContext.getObjectValue(), null);
			if(apply==null || !apply)
				return false;
		}
		
		return true;
	}
	
	@Override
	public Object doExecute(CellContext cellContext, ExecutorModel executorModel, Object preResult) {
		String sumValueField = executorModel.getAttributes().get(SUM_VALUE_FIELD);
		Object fieldvalue = cellContext.getFieldValue();
		if(StringUtils.isNotBlank(sumValueField)){
//			fieldvalue = cellContext.getParser().parseValue(sumValueField, cellContext.getObjectValue(), null);
			CellContext sumFieldCellContext = cellContext.getRowContext().getCellContext(sumValueField);
			if(sumFieldCellContext==null)
				throw new BaseException("not found cell for name: " + sumValueField);
			fieldvalue = sumFieldCellContext.getFieldValue();
		}else{
			fieldvalue = cellContext.getFieldValue().toString();
		}
		
		Double total = null;
		if(preResult==null)
			total = 0D;
		else
			total = (Double)preResult;
		
		try {
			total = total + Double.parseDouble(fieldvalue.toString());
		} catch (Exception e) {
		}
//		logger.info(cellContext.getField().getLabel()+" preResult: "+preResult+" now: "+cellContext.getFieldValue().toString()+" total: "+total);
		return total;
	}

	
	

}
