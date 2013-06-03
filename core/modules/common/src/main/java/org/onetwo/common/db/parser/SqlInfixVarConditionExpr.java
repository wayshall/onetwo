package org.onetwo.common.db.parser;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.lexer.AbstractParser.JTokenValue;
import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;



public class SqlInfixVarConditionExpr extends SqlInfixConditionExpr implements SqlVarObject {

	private boolean rightVar = true;
	private String varname = null;
	private boolean named = false;
	public SqlInfixVarConditionExpr() {
		super();
	}

	public SqlInfixVarConditionExpr(JTokenValueCollection<SqlTokenKey> left, SqlTokenKey operator, JTokenValueCollection<SqlTokenKey> right, boolean rightVar) {
		super(left, operator, right);
		this.rightVar = rightVar;
		JTokenValueCollection<SqlTokenKey> varIds = null;
		if(isRightVar()){
			varIds = right;
		}else{
			varIds = left;
		}
		named = varIds.contains(SqlTokenKey.VARNAME);
		if(named){
			JTokenValue<SqlTokenKey> tv = varIds.getTokenValue(SqlTokenKey.VARNAME);
			if(tv==null)
				throw new BaseException("not found var name in : " + varIds);
			varname = tv.getValue().substring(1);
		}else{
			varname = SqlTokenKey.QUESTION.getName();
		}
	}

	@Override
	public String getVarname() {
		return varname;
	}

	@Override
	public boolean isNamed() {
		return named;
	}

	public boolean isRightVar() {
		return rightVar;
	}
	
	public String getLeftString(){
		return getLeftString(1);
	}
	
	protected String getLeftString(int varCount){
		if(!isNamed()){
//			return getLeft().getValues("");
//			return getLeft().getVauesWithReplace(" ", SqlTokenKey.QUESTION, getActualPlaceHolder(varCount));
			return SqlParserUtils.toSqlWithReplace(getLeft(), SqlTokenKey.QUESTION, varCount);
		}
		
		if(isRightVar()){
//			return getLeft().getValues("");
			return SqlParserUtils.toFragmentSql(getLeft());
		}else{
//			return getLeft().getVauesWithReplace(" ", SqlTokenKey.VARNAME, getActualPlaceHolder(varCount));
			return SqlParserUtils.toSqlWithReplace(getLeft(), SqlTokenKey.VARNAME, varCount);
		}
	}
	
	protected String getRightString(int varCount){
		if(!isNamed()){
//			return getRight().getValues("");
//			return getRight().getVauesWithReplace(" ", SqlTokenKey.QUESTION, getActualPlaceHolder(varCount));
			return SqlParserUtils.toSqlWithReplace(getRight(), SqlTokenKey.QUESTION, varCount);
		}
		
		if(isRightVar()){
//			return getRight().getVauesWithReplace(" ", SqlTokenKey.VARNAME, getActualPlaceHolder(varCount));
			return SqlParserUtils.toSqlWithReplace(getRight(), SqlTokenKey.VARNAME, varCount);
		}else{
//			return getRight().getValues("");
			return SqlParserUtils.toFragmentSql(getRight());
		}
	}

	@Override
	public boolean isInfix() {
		return true;
	}

	@Override
	public String toJdbcSql(int varCount){
		StringBuilder sql = new StringBuilder();
		sql.append(getLeftString(varCount)).append(getOperatorString()).append(" ").append(getRightString(varCount));
		return sql.toString();
	}


}
