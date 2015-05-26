package org.onetwo.common.db.sqlext;

import org.onetwo.common.db.QueryField;


public interface HqlSymbolParser {
	
	/***
	 * 操作符
	 * @return
	 */
	public String getMappedOperator();
	
	public String parse(QueryField field);

}
