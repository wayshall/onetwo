package org.onetwo.ext.poi.excel.etemplate.directive;

import org.onetwo.ext.poi.excel.etemplate.ETSheetContext.ETRowContext;



public interface ETRowDirective {

	String getName();
	
	/*public T matchStart(ETSheetContext sheetContext);

	public boolean matchEnd(ETRowContext context);*/
	
	public boolean isMatch(ETRowContext rowContext);

	public boolean excecute(ETRowContext rowContext);

}