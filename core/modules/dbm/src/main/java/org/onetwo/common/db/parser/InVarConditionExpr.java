package org.onetwo.common.db.parser;

import java.util.List;

import org.onetwo.common.db.sqlext.SQLKeys;
import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;


public class InVarConditionExpr extends SqlInfixVarConditionExpr implements SqlVarObject {

	public InVarConditionExpr(JTokenValueCollection<SqlTokenKey> left, SqlTokenKey operator, JTokenValueCollection<SqlTokenKey> right, boolean rightVar) {
		super(left, operator, right, rightVar);
	}

	@Override
	public String parseSql(SqlCondition condition){
		if(SQLKeys.Null==condition.getValue()){
			StringBuilder sql = new StringBuilder();
			this.parseSingleValue(sql, condition, condition.getValue(), 0);
			return sql.toString();
		}
		if (condition.isMutiValue()) {
			return toJdbcSql(condition.getValue(List.class).size());
		} else {
			return toJdbcSql(1);
		}
	}
	

}
