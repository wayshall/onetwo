package org.onetwo.common.db.sqlext;

import java.util.List;

import org.onetwo.common.db.builder.QueryField;
import org.onetwo.common.db.sqlext.ExtQuery.K.IfNull;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Assert;
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
	protected final String mappedOperator;
	protected final String actualOperator;
	
	AbstractSQLSymbolParser(String symbol){
		Assert.hasText(symbol);
		this.mappedOperator = symbol;
		this.actualOperator = symbol;
	}
	
	AbstractSQLSymbolParser(String mappedOperator, String actualOperator){
		Assert.hasText(mappedOperator);
		Assert.hasText(actualOperator);
		this.mappedOperator = mappedOperator;
		this.actualOperator = actualOperator;
	}
	
	public String getMappedOperator() {
		return mappedOperator;
	}

	@Override
	public String parse(QueryField field) {
		return parse(getActualDbOperator(field), field);
	}

	public String getActualDbOperator(QueryField field){
		return actualOperator;
	}
	/*public String parse(String field, Object value, ParamValues paramValues, IfNull ifNull){
		return parse(field, null, value, paramValues, ifNull);
	}*/

	/*protected void processKey(String field, String symbol, SQLKeys key, StringBuilder hql){
		//process SQLKeys
		if(SQLKeys.Null==key){
			if(FieldOP.eq.equals(symbol)){
				hql.append(field).append(" is null ");
			}else if(FieldOP.neq.equals(symbol) || FieldOP.neq2.equals(symbol)){
				hql.append(field).append(" is not null ");
			}else{
				LangUtils.throwBaseException("unsupported symbol: " + symbol);
			}
		}
	}*/
	
	public String parse(String actualOperator, QueryField qfield){
		if(StringUtils.isBlank(actualOperator))
			LangUtils.throwBaseException("symbol can not be blank : " + actualOperator);
		
		String field = qfield.getActualFieldName();
		Object value = qfield.getValue();
		ParamValues paramValues = qfield.getExtQuery().getParamsValue();
		IfNull ifNull = qfield.getExtQuery().getIfNull();
		
		List list = convertValues(field, value, ifNull, true);
		
		field = getFieldName(field);
		StringBuilder hql = new StringBuilder();
		if(this.subQuery(field, actualOperator, list, paramValues, hql)){
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
			/*if(v instanceof SQLKeys){
				SQLKeys key = (SQLKeys) v;
//				hql.append(field).append(" ").append(symbolAlias).append(" ").append(key.getValue()).append(" ");
				this.processKey(field, symbol, key, hql);
			}
			else{
				process(field, symbol, i, v, hql, paramValues);
			}*/
			process(field, actualOperator, i, v, hql, paramValues);
			
			if(i!=list.size()-1)
				hql.append(" or ");
		}
		if(mutiValue)
			hql.append(") ");
		
		return hql.toString();
	}
	
	protected void process(String field, String actualOperator, int index, Object value, StringBuilder sqlScript, ParamValues paramValues){
		sqlScript.append(field).append(" ").append(actualOperator).append(" ");
		paramValues.addValue(field, value, sqlScript);
		sqlScript.append(" ");
	}
	
	protected List convertValues(Object fields, Object values, IfNull ifNull){
		return ExtQueryUtils.processValue(fields, values, ifNull, false);
	}
	
	protected List convertValues(Object fields, Object values, IfNull ifNull, boolean trimNull){
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
