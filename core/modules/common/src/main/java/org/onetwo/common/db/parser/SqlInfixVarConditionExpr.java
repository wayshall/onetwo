package org.onetwo.common.db.parser;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.db.sqlext.SQLKeys;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.lexer.AbstractParser.JTokenValue;
import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;



public class SqlInfixVarConditionExpr extends AbstractSqlVarObject implements SqlVarObject {

	private SqlInfixConditionExpr expr;
	private boolean rightVar = true;
	public SqlInfixVarConditionExpr() {
		super();
	}

	public SqlInfixVarConditionExpr(JTokenValueCollection<SqlTokenKey> left, SqlTokenKey operator, JTokenValueCollection<SqlTokenKey> right, boolean rightVar) {
		super();
		this.expr = new SqlInfixConditionExpr(left, operator, right);
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
			return SqlParserUtils.toSqlWithReplace(expr.getLeft(), SqlTokenKey.QUESTION, varCount);
		}
		
		if(isRightVar()){
//			return getLeft().getValues("");
			return SqlParserUtils.toFragmentSql(expr.getLeft());
		}else{
//			return getLeft().getVauesWithReplace(" ", SqlTokenKey.VARNAME, getActualPlaceHolder(varCount));
			return SqlParserUtils.toSqlWithReplace(expr.getLeft(), SqlTokenKey.VARNAME, varCount);
		}
	}
	
	protected String getRightString(int varCount){
		if(!isNamed()){
//			return getRight().getValues("");
//			return getRight().getVauesWithReplace(" ", SqlTokenKey.QUESTION, getActualPlaceHolder(varCount));
			return SqlParserUtils.toSqlWithReplace(expr.getRight(), SqlTokenKey.QUESTION, varCount);
		}
		
		if(isRightVar()){
//			return getRight().getVauesWithReplace(" ", SqlTokenKey.VARNAME, getActualPlaceHolder(varCount));
			return SqlParserUtils.toSqlWithReplace(expr.getRight(), SqlTokenKey.VARNAME, varCount);
		}else{
//			return getRight().getValues("");
			return SqlParserUtils.toFragmentSql(expr.getRight());
		}
	}

//	@Override
	public boolean isInfix() {
		return true;
	}

//	@Override
	public String toJdbcSql(int varCount){
		StringBuilder sql = new StringBuilder();
		sql.append(getLeftString(varCount)).append(expr.getOperatorString()).append(" ").append(getRightString(varCount));
		return sql.toString();
	}

	@Override
	public String parseSql(SqlCondition condition){
		StringBuilder sql = new StringBuilder();
		if (condition.isMutiValue()) {
			sql.append("( ");
			int vindex = 0;
			List<?> values = new ArrayList<Object>(condition.getValue(List.class));
			for (Object val : values) {
				if (vindex != 0)
					sql.append("or ");
				parseSingleValue(sql, condition, val, vindex);
				vindex++;
			}
			sql.append(")");
		} else {
			parseSingleValue(sql, condition, condition.getValue(), 0);
		}
		return sql.toString();
	}
	
	protected void parseSingleValue(StringBuilder sql, SqlCondition condition, Object val, int valueIndex) {
		if (SQLKeys.Null.equals(val)) {
			SQLKeys sk = (SQLKeys)val;
			String name = getLeftString();
			if (SqlTokenKey.NEQ==expr.getOperator() || SqlTokenKey.LTGT==expr.getOperator()) {
				sql.append(name).append("is not null ");
			}else {
				sql.append(name).append("is null ");
//				sql.append(toJdbcSql(1));
			}
			if (condition.isMutiValue()) {
				condition.getValue(List.class).set(valueIndex, sk.getJavaValue());
			} else {
				condition.setValue(sk.getJavaValue());// set to null, ignore
			}
		} else {
			if (SqlTokenKey.LIKE==expr.getOperator()) {
				String valStr = val == null ? "" : val.toString();
				
				if(isRightVar()){
					valStr = ExtQueryUtils.getLikeString(valStr);
				}
				if (condition.isMutiValue()) {
					condition.getValue(List.class).set(valueIndex, valStr);
				} else {
					condition.setValue(valStr);
				}
			}
			sql.append(this.toJdbcSql(1));
		}
	}

	
	
	/*@Override
	public String getVarname(int varIndex) {
		return getVarname();
	}

	@Override
	public int getVarCount() {
		return 1;
	}*/


}
