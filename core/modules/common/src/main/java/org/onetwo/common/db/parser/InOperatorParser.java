package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;

public class InOperatorParser implements OperatorParser {
	

	@Override
	public SqlTokenKey getOperator() {
		return SqlTokenKey.IN;
	}

	@Override
	public SqlObject parse(SqlParser parser, JTokenValueCollection<SqlTokenKey> leftOperatorTokens, JTokenValueCollection<SqlTokenKey> rigthOperatorTokens) {
		InVarConditionExpr inVar = null;
		if(leftOperatorTokens.contains(SqlTokenKey.VARNAME) || leftOperatorTokens.contains(SqlTokenKey.QUESTION)){
			inVar = new InVarConditionExpr(leftOperatorTokens.clone(), getOperator(), rigthOperatorTokens, false);
		}else if(rigthOperatorTokens.contains(SqlTokenKey.VARNAME) || rigthOperatorTokens.contains(SqlTokenKey.QUESTION)){
			inVar = new InVarConditionExpr(leftOperatorTokens.clone(), getOperator(), rigthOperatorTokens, true);
		}
		
		return inVar;
	}


}
