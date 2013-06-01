package org.onetwo.common.db.sqlext;

import org.apache.log4j.Logger;
import org.onetwo.common.db.QueryField;


/***
 * 可用于解释一般的操作符，如=,<,> ……
 * @author weishao
 *
 */
public class CommonSQLSymbolParser extends AbstractSQLSymbolParser {
	protected Logger logger = Logger.getLogger(this.getClass());
	
//	public static final String LIKE = "like";
	
//	protected SQLSymbolManager sqlSymbolManager;
	protected String symbol;
	protected String symbolAlias;
	
//	protected boolean like;
	
	CommonSQLSymbolParser(SQLSymbolManager sqlSymbolManager){
		super(sqlSymbolManager);
	}

	CommonSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String symbol){
		this(sqlSymbolManager, symbol, symbol);
	}
	
	CommonSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String symbol, boolean like){
		this(sqlSymbolManager, symbol, symbol);
		this.like = like;
	}
	
	CommonSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String symbol, String symbolDesc){
		this(sqlSymbolManager);
		this.symbol = symbol;
		this.symbolAlias = symbolDesc;
	}
	
	public String getSymbol(QueryField context) {
		return symbol;
	}

	protected void processKey(String field, SQLKeys key, StringBuilder hql){
		hql.append(field).append(" ").append(symbolAlias).append(" ").append(key.getValue()).append(" ");
	}

}
