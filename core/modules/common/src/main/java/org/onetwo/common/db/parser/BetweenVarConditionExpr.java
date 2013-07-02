package org.onetwo.common.db.parser;

import org.onetwo.common.lexer.AbstractParser.JTokenValueCollection;
import org.onetwo.common.utils.LangUtils;


public class BetweenVarConditionExpr extends AbstractSqlVarObject implements SqlVarObject {
	
	private JTokenValueCollection<SqlTokenKey> field; 
	final private SqlTokenKey operator = SqlTokenKey.BETWEEN;
	private JTokenValueCollection<SqlTokenKey> start;
	private JTokenValueCollection<SqlTokenKey> end;
	private String startVar;
	private String endVar;

	public BetweenVarConditionExpr(JTokenValueCollection<SqlTokenKey> field, JTokenValueCollection<SqlTokenKey> start, JTokenValueCollection<SqlTokenKey> end) {
		this.field = field;
		this.start = start;
		this.end = end;

		this.startVar = SqlParserUtils.parseVarname(start);
		this.endVar = SqlParserUtils.parseVarname(end);
	}

	@Override
	public String getVarname() {
		return startVar;
	}

	@Override
	public String parseSql(SqlCondition condition) {
		StringBuilder sql = new StringBuilder();
		return sql.toString();
	}

	/*@Override
	public String[] getVarnames() {
		return new String[]{startVar, endVar};
	}*/

	@Override
	public String toFragmentSql() {
		return LangUtils.append(SqlParserUtils.toFragmentSql(field), operator.getName(), " ", startVar, " ", SqlTokenKey.AND, " ", endVar);
	}

	

}
