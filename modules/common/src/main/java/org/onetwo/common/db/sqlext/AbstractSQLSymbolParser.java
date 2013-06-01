package org.onetwo.common.db.sqlext;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.db.ParamValues;
import org.onetwo.common.db.QueryField;
import org.onetwo.common.db.sqlext.SQLSymbolManager.FieldOP;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;


/***
 * 可用于解释一般的操作符，如=,<,> ……
 * @author weishao
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
abstract public class AbstractSQLSymbolParser implements HqlSymbolParser{
	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected SQLSymbolManager sqlSymbolManager;
	protected boolean like;
	
	AbstractSQLSymbolParser(SQLSymbolManager sqlSymbolManager){
		this.sqlSymbolManager = sqlSymbolManager;
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
		String field = qfield.getActualFieldName();
		Object value = qfield.getValue();
		ParamValues paramValues = qfield.getExtQuery().getParamsValue();
		IfNull ifNull = qfield.getExtQuery().getIfNull();
		/*if(value==null || (value instanceof String && StringUtils.isBlank(value.toString())))
			return null;*/
		
//		List list = LangUtils.asList(value);//MyUtils.asList(value);
		/*if(list==null || list.isEmpty())
			return null;*/
		
		/*if(ExtQueryUtils.isContinueByCauseValue(list, ifNull)){
			return null;
		}*/
		
		List list = ExtQueryUtils.processValue(field, value, ifNull, true);
		
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

			if(isLike()){
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
			}
			
			if(i!=list.size()-1)
				hql.append(" or ");
		}
		if(mutiValue)
			hql.append(") ");
		
		return hql.toString();
	}
	
	protected void process(String field, String symbol, int index, Object value, StringBuilder sqlScript, ParamValues paramValues){
		if(StringUtils.isBlank(symbol))
			LangUtils.throwBaseException("symbol can not be blank : " + symbol);
		sqlScript.append(field).append(" ").append(symbol).append(" ");
		paramValues.addValue(field, value, sqlScript);
		sqlScript.append(" ");
	}

	/************
	 * map.put("userName:in", new Object[]{UserEntity.class, "userName", "age:>=", 25});
	 * ==>
	 * map.put("userName:in", new Object[]{UserEntity.class, K.select, "userName", "age:>=", 25});
	 * 
	 * @param field
	 * @param symbol
	 * @param paramlist
	 * @param paramValues
	 * @param hql
	 * @return
	 */
	protected boolean subQuery(String field, String symbol, List paramlist, ParamValues paramValues, StringBuilder hql){
		Object value1 = paramlist.get(0);
		if(value1 instanceof Class){
			if(paramlist.size()<2)
				throw new ServiceException("sub select is not enough args! it must great than 1.");
			
			hql.append(field).append(" ").append(symbol).append(" ( ");
			paramlist.remove(value1);
			Class subEntity = (Class)value1;
			ExtQuery subQuery = this.createSubQuery(subEntity, paramlist);
			paramValues.joinToQuery(subQuery);
			hql.append(subQuery.getSql());
			hql.append(") ");
			return true;
		}
		return false;
	}
	
	protected ExtQuery createSubQuery(Class subEntity, List paramlist){
		ExtQuery subQuery = null;
		String subAlias = "sub_"+StringUtils.uncapitalize(subEntity.getSimpleName());
		if(paramlist.size()%2==0)
			subQuery = this.sqlSymbolManager.createQuery(subEntity, subAlias, MyUtils.convertParamMap(paramlist.toArray()));
		else{
			paramlist.add(0, K.SELECT);//entity后第一个str为要select的字段
			subQuery = this.sqlSymbolManager.createQuery(subEntity, subAlias, MyUtils.convertParamMap(paramlist.toArray()));
		}
		subQuery.setSubQuery(true);
		return subQuery;
	}
	
	protected String getFieldName(String f){
		return f;
	}
	
	public boolean isLike(){
		return like;
	}

}
