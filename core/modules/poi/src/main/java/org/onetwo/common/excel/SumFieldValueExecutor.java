package org.onetwo.common.excel;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.excel.data.CellContextData;
import org.onetwo.common.excel.exception.ExcelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SumFieldValueExecutor extends AbstractFieldValueExecutor {

	public static final String SUM_VALUE_FIELD = "sumValueField";
	public static final String SUM_VALUE_CONDITION = "sumValueCondition";

	private static final Logger logger = LoggerFactory.getLogger(SumFieldValueExecutor.class);

	@Override
	public boolean apply(CellContextData cellContext, ExecutorModel executorModel) {
		if(!cellContext.getFieldModel().getRow().isIterator())
			return false;
		
		String sumValueCondition = executorModel.getAttributes().get(SUM_VALUE_CONDITION);
		if(StringUtils.isNotBlank(sumValueCondition)){
			Boolean apply = (Boolean)cellContext.parseValue(sumValueCondition);
			if(apply==null || !apply)
				return false;
		}
		
		return true;
	}
	
	@Override
	public Object doExecute(CellContextData cellContext, ExecutorModel executorModel, Object preResult) {
		String sumValueField = executorModel.getAttributes().get(SUM_VALUE_FIELD);
		Object fieldvalue = cellContext.getFieldValue();
		if(StringUtils.isNotBlank(sumValueField)){
//			fieldvalue = cellContext.getParser().parseValue(sumValueField, cellContext.getObjectValue(), null);
			CellContextData sumFieldCellContext = cellContext.getRowContext().getCellContext(sumValueField);
			if(sumFieldCellContext==null)
				throw new ExcelException("not found cell for name: " + sumValueField);
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
