package org.onetwo.common.excel.etemplate;

abstract public class ExcelObject {
	
	abstract public void accept(ExcelVisitor visitor);

}
