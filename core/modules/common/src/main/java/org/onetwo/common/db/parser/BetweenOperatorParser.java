package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;

public class BetweenOperatorParser implements OperatorParser {
	

	@Override
	public SqlTokenKey getOperator() {
		return SqlTokenKey.BETWEEN;
	}

	@Override
	public SqlObject parse(SqlParser parser, JTokenValueCollection<SqlTokenKey> leftOperatorTokens, JTokenValueCollection<SqlTokenKey> rigthOperatorTokens) {
		System.out.println("left: " + leftOperatorTokens);
		return null;
	}


}
