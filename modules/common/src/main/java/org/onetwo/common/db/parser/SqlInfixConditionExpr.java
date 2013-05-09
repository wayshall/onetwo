package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;
import org.onetwo.common.utils.LangUtils;


public class SqlInfixConditionExpr extends SqlObjectImpl {

	private JTokenValueCollection<SqlTokenKey> left;
	private SqlTokenKey operator;
	private JTokenValueCollection<SqlTokenKey> right;
	
	public SqlInfixConditionExpr(){
		super("");
	}

	public SqlInfixConditionExpr(JTokenValueCollection<SqlTokenKey> left, SqlTokenKey operator, JTokenValueCollection<SqlTokenKey> right) {
		super("");
		this.operator = operator;
		this.left = left;
		this.right = right;
	}
	public JTokenValueCollection<SqlTokenKey> getRight() {
		return right;
	}

	public void setRight(JTokenValueCollection<SqlTokenKey> right) {
		this.right = right;
	}

	public JTokenValueCollection<SqlTokenKey> getLeft() {
		return left;
	}

	public void setLeft(JTokenValueCollection<SqlTokenKey> left) {
		this.left = left;
	}

	public SqlTokenKey getOperator() {
		return operator;
	}
	public String getOperatorString() {
		return operator.getName();
	}

	@Override
	public String toFragmentSql() {
		return LangUtils.append(SqlParserUtils.toFragmentSql(getLeft()), operator.getName(), " ", SqlParserUtils.toFragmentSql(getRight()));
	}


}
