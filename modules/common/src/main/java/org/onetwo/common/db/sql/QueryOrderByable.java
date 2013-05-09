package org.onetwo.common.db.sql;

public interface QueryOrderByable {

	
	public void asc(String... fields);
	
	public void desc(String... fields);
	
}
