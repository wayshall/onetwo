package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;


abstract public class AbstractSqlVarObject extends SqlObjectImpl implements SqlVarObject{

	protected String varname = null;
	protected boolean named = false;
	
	public AbstractSqlVarObject() {
		super();
	}

	public AbstractSqlVarObject(String fragmentSql) {
		super(fragmentSql);
	}
	
	@Override
	public String getVarname() {
		return varname;
	}

//	@Override
	public boolean isNamed() {
		return named;
	}

	protected String getString(JTokenValueCollection<SqlTokenKey> tokens, int varCount){
		if(isNamed()){
			return SqlParserUtils.toSqlWithReplace(tokens, SqlTokenKey.VARNAME, varCount);
		}else{
			return SqlParserUtils.toSqlWithReplace(tokens, SqlTokenKey.QUESTION, varCount);
		}
	}
}
