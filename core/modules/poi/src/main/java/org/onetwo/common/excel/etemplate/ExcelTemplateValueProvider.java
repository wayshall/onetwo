package org.onetwo.common.excel.etemplate;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.excel.ExcelUtils;
import org.onetwo.common.excel.etemplate.RowForeachDirectiveModel.ForeachRowInfo;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Expression;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.ValueProvider;
import org.slf4j.Logger;

public class ExcelTemplateValueProvider implements ValueProvider {

	protected final Logger logger = JFishLoggerFactory.logger(this.getClass());
	
	final private ETemplateContext templateContext;
	private List<CellRangeAddress> cellRangeList;
	private boolean debug = false;
	private final Expression expression = Expression.DOLOR;
	
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
			final String text = expression.parseByProvider(cellText, provider);
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
		return StringUtils.emptyIfNull(val);
	}
	
	public Object parseValue(String exp){
		Object val = ExcelUtils.getValue(exp, templateContext.getDataContext(), templateContext.getRootObject());
		return val;
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
		if(LangUtils.isEmpty(cellRangeList))
			return null;
		for(CellRangeAddress cr : cellRangeList){
			if(cr.getFirstRow()==rownum){
				return cr;
			}
		}
		return null;
	}
	
	public CellRangeAddress getCellRange(ForeachRowInfo row, Cell cell){
		if(LangUtils.isEmpty(cellRangeList))
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
