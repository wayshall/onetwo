package org.onetwo.common.excel.etemplate;

public class ExcelCell extends ExcelObject{

	@Override
	public void accept(ExcelVisitor visitor) {
		visitor.visit(this);
	}
	
}
