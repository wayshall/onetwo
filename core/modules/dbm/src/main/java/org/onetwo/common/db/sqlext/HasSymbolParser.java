package org.onetwo.common.db.sqlext;


/****
 * 对in操作符的解释
 * 
 * @author weishao
 *
 */
public class HasSymbolParser extends CommonSQLSymbolParser implements HqlSymbolParser {
	
	public HasSymbolParser(SQLSymbolManager sqlSymbolManager){
		super(sqlSymbolManager, "member of");
	}
	
	protected void process(String field, String symbol, int index, Object value, StringBuilder sqlScript, ParamValues paramValues){
//		super.process(field, symbol, index, value, sqlScript, paramValues);
		paramValues.addValue(field, value, sqlScript);
		sqlScript.append(" ").append(symbol).append(" ").append(field).append(" ");
	}
	
}
