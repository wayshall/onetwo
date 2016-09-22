package org.onetwo.dbm.mapping;


public class SimpleJdbcStatementContext<T> implements JdbcStatementContext<T> {


	public static <T> JdbcStatementContext<T> create(EntrySQLBuilder sqlBuilder, T values){
		return new SimpleJdbcStatementContext<T>(sqlBuilder, values);
	}
	
	private final String sql;
	private final T values;
	private final EntrySQLBuilder sqlBuilder;
	
	private SimpleJdbcStatementContext(EntrySQLBuilder sqlBuilder, T values) {
		super();
		this.sql = sqlBuilder.getSql();
		this.values = values;
		this.sqlBuilder = sqlBuilder;
	}
	@Override
	public String getSql() {
		return sql;
	}
	
	@Override
	public T getValue() {
		return values;
	}
	@Override
	public EntrySQLBuilder getSqlBuilder() {
		return sqlBuilder;
	}
}
