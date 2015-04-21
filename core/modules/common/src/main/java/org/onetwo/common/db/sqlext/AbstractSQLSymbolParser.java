package org.onetwo.common.db.sqlext;

import java.util.List;

import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.db.ParamValues;
import org.onetwo.common.db.QueryField;
import org.onetwo.common.db.sqlext.SQLSymbolManager.FieldOP;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;


/***
 * 可用于解释一般的操作符，如=,<,> ……
 * @author weishao
 *
 */
@SuppressWarnings({"rawtypes"})
abstract public class AbstractSQLSymbolParser implements HqlSymbolParser{
	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
//	protected boolean like;
	protected final String symbol;
	
	AbstractSQLSymbolParser(String symbol){
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}

	@Override
	public String parse(QueryField field) {
		return parse(getSymbol(field), field);
	}

	abstract public String getSymbol(QueryField field);
	/*public String parse(String field, Object value, ParamValues paramValues, IfNull ifNull){
		return parse(field, null, value, paramValues, ifNull);
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
	
	public String parse(String symbol, QueryField qfield){
		if(StringUtils.isBlank(symbol))
			LangUtils.throwBaseException("symbol can not be blank : " + symbol);
		
		String field = qfield.getActualFieldName();
		Object value = qfield.getValue();
		ParamValues paramValues = qfield.getExtQuery().getParamsValue();
		IfNull ifNull = qfield.getExtQuery().getIfNull();
		
		List list = processValue(field, value, ifNull, true);
		
		field = getFieldName(field);
		StringBuilder hql = new StringBuilder();
		if(this.subQuery(field, symbol, list, paramValues, hql)){
			return hql.toString();
		}
		boolean mutiValue = list.size()>1;
		Object v = null;
		if(mutiValue)
			hql.append("(");
		for(int i=0; i<list.size(); i++){
			v = list.get(i);

			/*if(isLike()){
				if(!(v instanceof String))
					throw new ServiceException("the symbol is [like], the value must a string type!");
				v = ExtQueryUtils.getLikeString(list.get(i).toString());
				process(field, symbol, i, v, hql, paramValues);
			}else if(v instanceof SQLKeys){
				SQLKeys key = (SQLKeys) v;
//				hql.append(field).append(" ").append(symbolAlias).append(" ").append(key.getValue()).append(" ");
				this.processKey(field, symbol, key, hql);
			}
			else{
				process(field, symbol, i, v, hql, paramValues);
			}*/
			if(v instanceof SQLKeys){
				SQLKeys key = (SQLKeys) v;
//				hql.append(field).append(" ").append(symbolAlias).append(" ").append(key.getValue()).append(" ");
				this.processKey(field, symbol, key, hql);
			}
			else{
				process(field, symbol, i, v, hql, paramValues);
			}
			
			if(i!=list.size()-1)
				hql.append(" or ");
		}
		if(mutiValue)
			hql.append(") ");
		
		return hql.toString();
	}
	
	protected void process(String field, String symbol, int index, Object value, StringBuilder sqlScript, ParamValues paramValues){
		sqlScript.append(field).append(" ").append(symbol).append(" ");
		paramValues.addValue(field, value, sqlScript);
		sqlScript.append(" ");
	}
	
	public List processValue(Object fields, Object values, IfNull ifNull){
		return ExtQueryUtils.processValue(fields, values, ifNull, false);
	}
	
	public List processValue(Object fields, Object values, IfNull ifNull, boolean trimNull){
		return ExtQueryUtils.processValue(fields, values, ifNull, trimNull);
	}
	
	protected boolean subQuery(String field, String symbol, List paramlist, ParamValues paramValues, StringBuilder hql){
		return false;
	}
	
	
	protected String getFieldName(String f){
		return f;
	}
	
	/*public boolean isLike(){
		return like;
	}*/

}
