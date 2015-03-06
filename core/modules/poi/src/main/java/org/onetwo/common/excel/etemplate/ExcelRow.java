package org.onetwo.common.excel.etemplate;

public class ExcelRow extends ExcelObject{

	@Override
	public void accept(ExcelVisitor visitor) {
		visitor.visit(this);
	}
	
	

}
