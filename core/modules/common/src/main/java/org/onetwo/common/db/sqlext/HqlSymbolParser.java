package org.onetwo.common.db.sqlext;

import org.onetwo.common.db.QueryField;


public interface HqlSymbolParser {
	
	public String getSymbol();
	
	public String parse(QueryField field);

}
