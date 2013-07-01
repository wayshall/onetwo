package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;

public interface OperatorParser {
	
	public SqlTokenKey getOperator();
	
	public SqlObject parse(SqlParser parser, JTokenValueCollection<SqlTokenKey> leftOperatorTokens, JTokenValueCollection<SqlTokenKey> rigthOperatorTokens); 

}
