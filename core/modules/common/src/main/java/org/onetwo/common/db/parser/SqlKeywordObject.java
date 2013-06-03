package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValue;


public class SqlKeywordObject extends SqlObjectImpl {

	private final SqlTokenKey token;
	
	public SqlKeywordObject(JTokenValue<SqlTokenKey> tv){
		this(tv.getToken(), tv.getValue());
	}
	public SqlKeywordObject(SqlTokenKey token, String fragmentSql) {
		super(fragmentSql+" ");
		this.token = token;
	}
	public SqlTokenKey getToken() {
		return token;
	}
}
