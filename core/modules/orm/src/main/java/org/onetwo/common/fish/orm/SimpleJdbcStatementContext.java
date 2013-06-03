package org.onetwo.common.fish.orm;

public class SimpleJdbcStatementContext<T> implements JdbcStatementContext<T> {


	public static <T> JdbcStatementContext<T> create(String sql, T values){
		return new SimpleJdbcStatementContext<T>(sql, values);
	}

	private final String sql;
	private final T value;
	
	private SimpleJdbcStatementContext(String sql, T values) {
		super();
		this.sql = sql;
		this.value = values;
	}
	@Override
	public String getSql() {
		return sql;
	}
	@Override
	public T getValue() {
		return value;
	}

}
