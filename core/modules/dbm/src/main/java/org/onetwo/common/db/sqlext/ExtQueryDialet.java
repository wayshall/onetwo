package org.onetwo.common.db.sqlext;

public interface ExtQueryDialet {

//	public String getFieldName(String field);

	public String getPlaceHolder(int position);
	
	public String getNamedPlaceHolder(String name, int position);
	
	public String getNullsOrderby(String nullsOrder);
}
