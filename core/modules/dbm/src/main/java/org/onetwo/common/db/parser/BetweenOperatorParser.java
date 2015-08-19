package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;

public class BetweenOperatorParser implements OperatorParser {
	

	@Override
	public SqlTokenKey getOperator() {
		return SqlTokenKey.BETWEEN;
	}

	@Override
	public SqlObject parse(JFishSqlParser parser, JTokenValueCollection<SqlTokenKey> fieldTokens, JTokenValueCollection<SqlTokenKey> startTokens) {
		System.out.println("left: " + startTokens);
		parser.nextTokenIs(SqlTokenKey.AND);
		JTokenValueCollection<SqlTokenKey> endTokens = parser.nextAllTokensUntilKeywords();
		
		BetweenVarConditionExpr between = new BetweenVarConditionExpr(fieldTokens.clone(), startTokens.clone(), endTokens.clone());
		return between;
	}


}
