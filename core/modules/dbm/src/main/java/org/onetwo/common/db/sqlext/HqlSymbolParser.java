package org.onetwo.common.db.sqlext;

import org.onetwo.common.db.builder.QueryField;


public interface HqlSymbolParser {
	
	/***
	 * 操作符
	 * @return
	 */
	public String getMappedOperator();
	
	public String parse(QueryField field);

}
