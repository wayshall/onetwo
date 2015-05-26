package org.onetwo.common.jfishdbm.mapping;


public interface JdbcStatementContext<T> {

	public String getSql();

	public T getValue();
	
	public EntrySQLBuilder getSqlBuilder();

}