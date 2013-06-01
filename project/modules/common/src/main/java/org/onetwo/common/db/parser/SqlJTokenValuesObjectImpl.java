package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;




public class SqlJTokenValuesObjectImpl extends SqlObjectImpl {

	protected JTokenValueCollection<SqlTokenKey> tokens;

	public SqlJTokenValuesObjectImpl(JTokenValueCollection<SqlTokenKey> tokens) {
		super(SqlParserUtils.toFragmentSql(tokens));
		this.tokens = tokens;
	}

	/*@Override
	public String toFragmentSql(){
		return SqlParserUtils.toSql(tokens);
	}*/

}
