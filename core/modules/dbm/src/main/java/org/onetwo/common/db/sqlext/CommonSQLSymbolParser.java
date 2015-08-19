package org.onetwo.common.db.sqlext;

import org.onetwo.common.db.sqlext.SQLSymbolManager.FieldOP;
import org.onetwo.common.utils.LangUtils;


/***
 * 可用于解释一般的操作符，如=,<,> ……
 * @author weishao
 *
 */
public class CommonSQLSymbolParser extends AbstractSupportedSubQuerySQLSymbolParser {
	
//	public static final String LIKE = "like";
	
//	protected SQLSymbolManager sqlSymbolManager;
//	protected String symbolAlias;
//	private String mappedSymbol;
	
//	protected boolean like;

	CommonSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String symbol){
		super(sqlSymbolManager, symbol);
	}

	CommonSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String mappedOperator, String actualOperator){
		super(sqlSymbolManager, mappedOperator, actualOperator);
//		this.mappedSymbol = mappedSymbol;
	}
	
	/*CommonSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String symbol, boolean like){
		this(sqlSymbolManager, symbol, symbol);
		this.like = like;
	}*/
	
	/*CommonSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String symbol, String symbolDesc){
		super(sqlSymbolManager, symbol);
		this.symbolAlias = symbolDesc;
	}*/
	
	
	protected void processKey(String field, String symbol, SQLKeys key, StringBuilder hql){
		if(SQLKeys.Null==key){
			if(FieldOP.eq.equals(symbol)){
				hql.append(field).append(" is null ");
			}else if(FieldOP.neq.equals(symbol) || FieldOP.neq2.equals(symbol)){
				hql.append(field).append(" is not null ");
			}else{
				LangUtils.throwBaseException("unsupported symbol: " + symbol);
			}
		}
	}
	
	protected void process(String field, String symbol, int index, Object value, StringBuilder sqlScript, ParamValues paramValues){
		if(value instanceof SQLKeys){
			SQLKeys key = (SQLKeys) value;
			this.processKey(field, symbol, key, sqlScript);
		}else{
			super.process(field, symbol, index, value, sqlScript, paramValues);
		}
	}

	/*protected void processKey(String field, SQLKeys key, StringBuilder hql){
		hql.append(field).append(" ").append(symbolAlias).append(" ").append(key.getValue()).append(" ");
	}*/

}
