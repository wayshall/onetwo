package org.onetwo.ext.poi.excel.etemplate;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.expr.Expression;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.expr.ValueProvider;
import org.onetwo.ext.poi.excel.etemplate.directive.ForeachRowDirectiveModel.ForeachRowInfo;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OgnlExcelTemplateValueProvider implements ValueProvider, ExcelTemplateValueProvider {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	final private ETemplateContext templateContext;
	private List<CellRangeAddress> cellRangeList;
	private boolean debug = false;
	private final Expression expression = ExpressionFacotry.DOLOR;
	
	public OgnlExcelTemplateValueProvider(ETemplateContext context) {
		super();
		this.templateContext = context;
	}
	

	public void setCellValue(Cell cell, Object value){
		ExcelUtils.setCellValue(cell, value);
		
		/*if(value==null){
			return ;
		}
		不修改样式
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				cell.setCellValue(Types.convertValue(value, Double.class));
				break;
			case Cell.CELL_TYPE_STRING:
				cell.setCellValue(value.toString());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cell.setCellValue(Types.convertValue(value, Boolean.class));
				break;
			case Cell.CELL_TYPE_FORMULA:
				cell.setCellValue(Types.convertValue(value, Date.class));
				break;
	
			default:
				HSSFRichTextString cellValue = new HSSFRichTextString(value.toString());
				cell.setCellValue(cellValue);
				break;
		}*/
	}
	
	@Override
	public boolean isExpresstion(String cellText){
		return expression.isExpresstion(cellText);
	}

	public Object parseCellValue(String cellText){
		if (expression.isProperty(cellText)) {
			String exp = expression.getProperty(cellText);
			Object cellValue = parseValue(exp);
			return cellValue;
		}
		final String text = expression.parse(cellText, this);
		if(this.isDebug())
			logger.info("parse [{}] as [{}]", cellText, text);
		return text;
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
