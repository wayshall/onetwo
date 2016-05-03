package org.onetwo.common.excel.etemplate;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.excel.etemplate.directive.ForeachRowDirectiveModel.ForeachRowInfo;
import org.onetwo.common.excel.utils.ExcelUtils;
import org.onetwo.common.excel.utils.SimpleExcelExpression;
import org.onetwo.common.excel.utils.SimpleExcelExpression.ValueProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelTemplateValueProvider implements ValueProvider {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	final private ETemplateContext templateContext;
	private List<CellRangeAddress> cellRangeList;
	private boolean debug = false;
	private final SimpleExcelExpression expression = new SimpleExcelExpression("${", "}");
	
	public ExcelTemplateValueProvider(ETemplateContext context) {
		super();
		this.templateContext = context;
	}

	public Object parseCellValue(Cell cell, final ExcelTemplateValueProvider provider){
		Object cellValue = ExcelUtils.getCellValue(cell);
		if(cellValue==null)
			return null;
		String cellText = cellValue.toString();
		if(expression.isExpresstion(cellText)){
			final String text = expression.parse(cellText, provider);
//			ExcelUtils.setCellValue(cell, text);
			if(provider.isDebug())
				logger.info("parse [{}] as [{}]", cellText, text);
			return text;
		}else{
			return cellValue;
		}
	}

	public String findString(String var){
//		Object val = templateContext.get(var);
		Object val = parseValue(var);
		return val==null?"":val.toString();
	}
	
	public Object parseValue(String exp){
		Object val = ExcelUtils.getValue(exp, templateContext.getDataContext(), templateContext.getRootObject());
		return val;
	}
	
	public <T> T parseValue(String exp, Class<T> clazz){
		Object val = parseValue(exp);
		if(!clazz.isInstance(val)){
			throw new RuntimeException("the value must be " + clazz);
		}
		return clazz.cast(val);
	}

	public ETemplateContext getTemplateContext() {
		return templateContext;
	}

	public List<CellRangeAddress> getCellRangeList() {
		return cellRangeList;
	}

	public void setCellRangeList(List<CellRangeAddress> cellRangeList) {
		this.cellRangeList = cellRangeList;
	}
	
	public CellRangeAddress getCellRange(int rownum){
		if(ExcelUtils.isEmpty(cellRangeList))
			return null;
		for(CellRangeAddress cr : cellRangeList){
			if(cr.getFirstRow()==rownum){
				return cr;
			}
		}
		return null;
	}
	
	public CellRangeAddress getCellRange(ForeachRowInfo row, Cell cell){
		if(ExcelUtils.isEmpty(cellRangeList))
			return null;
		for(CellRangeAddress cr : cellRangeList){
			if(cr.getFirstRow()==row.getOriginRownum() && cr.getFirstColumn()==cell.getColumnIndex()){
				return cr;
			}
		}
		return null;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
