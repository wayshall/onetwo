package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValue;



public class SqlSymbolObject extends SqlObjectImpl {

	private final SqlTokenKey token;
	
	public SqlSymbolObject(JTokenValue<SqlTokenKey> token) {
		super(token.getValue());
		this.token = token.getToken();
	}
	public SqlTokenKey getToken() {
		return token;
	}
}
