package org.onetwo.common.excel.etemplate;

public interface ExcelVisitor {

	boolean visit(ExcelRow row);
	boolean visit(ExcelCell cell);

}
