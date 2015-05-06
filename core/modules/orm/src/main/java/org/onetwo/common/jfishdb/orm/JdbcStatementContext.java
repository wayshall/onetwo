package org.onetwo.common.jfishdb.orm;


public interface JdbcStatementContext<T> {

	public String getSql();

	public T getValue();
	
	public EntrySQLBuilder getSqlBuilder();

}