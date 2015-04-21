package org.onetwo.common.db.sqlext;

import java.util.HashSet;
import java.util.Set;

import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.db.ParamValues;
import org.onetwo.common.db.sqlext.SQLSymbolManager.FieldOP;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Assert;



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
		}
	};
	
	LikeSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String symbol) {
		super(sqlSymbolManager, symbol);
		Assert.notNull(symbol);
		if(!SYMBOL_SET.contains(symbol.toLowerCase())){
			throw new IllegalArgumentException("only support like or not like");
		}
	}

	protected void process(String field, String symbol, int index, Object value, StringBuilder sqlScript, ParamValues paramValues){
		if(!(value instanceof String))
			throw new ServiceException("the symbol is [like], the value must a string type!");
		value = ExtQueryUtils.getLikeString(value.toString());
		super.process(field, symbol, index, value, sqlScript, paramValues);
	}

}
