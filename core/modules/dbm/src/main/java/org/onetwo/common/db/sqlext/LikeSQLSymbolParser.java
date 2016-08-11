package org.onetwo.common.db.sqlext;

import java.util.HashSet;
import java.util.Set;

import org.onetwo.common.db.sqlext.SQLSymbolManager.FieldOP;
import org.onetwo.common.exception.ServiceException;



/***
 * 可用于解释一般的操作符，如=,<,> ……
 * @author weishao
 *
 */
public class LikeSQLSymbolParser extends CommonSQLSymbolParser {
	
	@SuppressWarnings("serial")
	private static Set<String> SYMBOL_SET = new HashSet<String>(){
		{
			add(FieldOP.like);
			add(FieldOP.not_like);
			add(FieldOP.like2);
			add(FieldOP.not_like2);
		}
	};
	

	LikeSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String mappedOperator) {
		this(sqlSymbolManager, mappedOperator, mappedOperator);
	}
	LikeSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String mappedOperator, String actualOperator) {
		super(sqlSymbolManager, mappedOperator, actualOperator);
		if(!SYMBOL_SET.contains(mappedOperator.toLowerCase())){
			throw new IllegalArgumentException("only support : " + SYMBOL_SET.toString());
		}
	}

	protected void process(String field, String symbol, int index, Object value, StringBuilder sqlScript, ParamValues paramValues){
		if(value!=null){
			if(!(value instanceof String))
				throw new ServiceException("the symbol is [like], the value must a string type, but " + value);
			value = ExtQueryUtils.getLikeString(value.toString());
		}
		super.process(field, symbol, index, value, sqlScript, paramValues);
	}

}
