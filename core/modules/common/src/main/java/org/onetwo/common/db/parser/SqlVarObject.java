package org.onetwo.common.db.parser;

public interface SqlVarObject {

	public String getVarname();

	public boolean isNamed();
	
	public boolean isInfix();
	
//	public String getActualPlaceHolder(int count);
	
	public String toJdbcSql(int varCount);
	
//	public String toJdbcSql();

}