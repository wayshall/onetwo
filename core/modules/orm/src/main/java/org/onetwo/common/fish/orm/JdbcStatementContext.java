package org.onetwo.common.fish.orm;


public interface JdbcStatementContext<T> {

	public String getSql();

	public T getValue();
	
	public EntrySQLBuilder getSqlBuilder();

}