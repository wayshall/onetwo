package org.onetwo.common.fish.orm;

import java.util.List;

public class SqlBuilderJdbcStatementContext implements JdbcStatementContext<List<Object[]>> {


	public static JdbcStatementContext<List<Object[]>> create(String sql, JdbcStatementContextBuilder builder){
		return new SqlBuilderJdbcStatementContext(sql, builder);
	}

	private final String sql;
	private final JdbcStatementContextBuilder builder;
	
	private SqlBuilderJdbcStatementContext(String sql, JdbcStatementContextBuilder builder) {
		super();
		this.sql = sql;
		this.builder = builder;
	}
	@Override
	public String getSql() {
		return sql;
	}
	@Override
	public List<Object[]> getValue() {
		return builder.getValues();
	}

}
