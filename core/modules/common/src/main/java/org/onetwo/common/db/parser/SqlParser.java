package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractLexer;

public interface SqlParser {
	
	SqlStatment parse();
	
	public AbstractLexer<SqlTokenKey> getLexer();

}
