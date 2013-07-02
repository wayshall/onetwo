package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;
import org.onetwo.common.utils.LangUtils;


public class BetweenVarConditionExpr extends AbstractSqlVarObject implements SqlVarObject {
	
	private JTokenValueCollection<SqlTokenKey> field; 
	final private SqlTokenKey operator = SqlTokenKey.BETWEEN;
	private JTokenValueCollection<SqlTokenKey> start;
	private JTokenValueCollection<SqlTokenKey> end;

	public BetweenVarConditionExpr(JTokenValueCollection<SqlTokenKey> field, JTokenValueCollection<SqlTokenKey> start, JTokenValueCollection<SqlTokenKey> end) {
		this.field = field;
		this.start = start;
		this.end = end;

		this.varname = SqlParserUtils.parseVarname(start);
		this.named = start.contains(SqlTokenKey.VARNAME);
	}

	@Override
	public String parseSql(SqlCondition condition) {
		StringBuilder sql = new StringBuilder();
		sql.append(SqlParserUtils.toFragmentSql(field))
//			.append(" ")
			.append(operator.getName())
			.append(" ")
			.append(getString(start, 1))
//			.append(" ")
			.append(SqlTokenKey.AND.getName())
			.append(" ")
			.append(getString(end, 1));
		
		return sql.toString();
	}
	

	/*@Override
	public String[] getVarnames() {
		return new String[]{startVar, endVar};
	}*/

	@Override
	public String toFragmentSql() {
		return LangUtils.append(SqlParserUtils.toFragmentSql(field), operator.getName(), " ", start, " ", SqlTokenKey.AND, " ", end);
	}

	

}
