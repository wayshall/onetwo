package org.onetwo.common.db.parser;


public interface SqlVarObject extends SqlObject {

	public String getVarname();
//	public int getVarsize();
//	public String[] getVarnames();

//	public String getVarname(int varIndex);
//	public int getVarCount();

//	public boolean isNamed();
	
//	public boolean isInfix();
	
//	public String getActualPlaceHolder(int count);
	
//	public String toJdbcSql(int varCount);
	
//	public String toJdbcSql();
	public String parseSql(SqlCondition condition);

}