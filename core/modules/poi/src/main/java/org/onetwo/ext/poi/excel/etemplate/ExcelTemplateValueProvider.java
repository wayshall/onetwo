package org.onetwo.ext.poi.excel.etemplate;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author wayshall
 * <br/>
 */
public interface ExcelTemplateValueProvider {

	boolean isExpresstion(String cellText);

	/***
	 * 直接解释cell整个文本的值，通常是文本和表达式混杂
	 * 如：text-${exp}-text2
	 * @author wayshall
	 * @param cellText
	 * @return
	 */
	Object parseCellValue(String cellText);
	
	/***
	 * 解释表达式的值
	 * @author wayshall
	 * @param exp
	 * @return
	 */
	Object parseValue(String exp);
	
	void setCellValue(Cell cell, Object value);
	
	void setCellRangeList(List<CellRangeAddress> cellRangeList);
	
	ETemplateContext getTemplateContext();
	
	boolean isDebug();

}