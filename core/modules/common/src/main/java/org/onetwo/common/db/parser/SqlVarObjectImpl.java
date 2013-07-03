package org.onetwo.common.db.parser;



public class SqlVarObjectImpl extends AbstractSqlVarObject implements SqlVarObject{
	
	private final String varname;
	private final boolean named;
	
	public SqlVarObjectImpl(String varname) {
		super(varname);
		this.named = !SqlTokenKey.QUESTION.getName().equals(varname);

		String str = "";
		if(named){
			str = varname.substring(1);
		}else{
			str = SqlTokenKey.QUESTION.getName();
		}
		this.varname = str;
	}

	@Override
	public String getVarname() {
		return varname;
	}

//	@Override
	public boolean isNamed() {
		return named;
	}

//	@Override
	public boolean isInfix() {
		return false;
	}

//	@Override
	public String getActualPlaceHolder(int count) {
		return SqlParserUtils.getActualPlaceHolder(count, count>1);
	}

//	@Override
	public String toJdbcSql(int varCount) {
		return getActualPlaceHolder(varCount);
	}

	/*@Override
	public String getVarname(int varIndex) {
		return getVarname();
	}

	@Override
	public int getVarCount() {
		return 1;
	}*/

	@Override
	public String parseSql(SqlCondition condition){
		return toJdbcSql(1);
	}

}
