package org.onetwo.common.db.sqlext;

import org.onetwo.common.db.builder.QueryField;
import org.onetwo.common.utils.LangUtils;

public class BooleanValueSQLSymbolParser extends AbstractSupportedSubQuerySQLSymbolParser {

	private String whenTrue;
	private String whenFalse;
	
	public BooleanValueSQLSymbolParser(SQLSymbolManager sqlSymbolManager, String symbol, String whenTrue, String whenFalse) {
		super(sqlSymbolManager, symbol);
		this.whenTrue = whenTrue;
		this.whenFalse = whenFalse;
	}
	

	@Override
	public String getActualDbOperator(QueryField context) {
		Object value = context.getValue();
		if(LangUtils.isMultiple(value)){
			LangUtils.throwBaseException("symbol[${0}] is unsupport mutil value : " + LangUtils.toString(value));
		}
		if(!(value instanceof Boolean))
			LangUtils.throwBaseException(LangUtils.toString("symbol[${0}] is unsupport the type : ${1}", (whenTrue+" | "+whenFalse), value==null?"NULL":value.getClass()));
		boolean val = (Boolean) context.getValue();
		String symbol;
		if(val)
			symbol = whenTrue;
		else
			symbol = whenFalse;
		return symbol;
	}

	@Override
	protected void process(String field, String symbol, int index, Object value, StringBuilder sqlScript, ParamValues paramValues) {
		/*if(!(value instanceof Boolean))
			LangUtils.throwBaseException(LangUtils.toString("symbol[${0}] is unsupport the type : ${1}", symbol, value==null?"NULL":value.getClass()));
		boolean val = (Boolean) value;
		if(val)
			symbol = whenTrue;
		else
			symbol = whenFalse;*/
		sqlScript.append(field).append(" ").append(symbol).append(" ");
	}

}
