package org.onetwo.common.db.sqlext;

import java.util.List;

import org.onetwo.common.db.sqlext.ExtQuery.K;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;


/***
 * 可用于解释一般的操作符，如=,<,> ……
 * @author weishao
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
abstract public class AbstractSupportedSubQuerySQLSymbolParser extends AbstractSQLSymbolParser {
	
	protected SQLSymbolManager sqlSymbolManager;
	
	AbstractSupportedSubQuerySQLSymbolParser(SQLSymbolManager sqlSymbolManager, String symbol){
		super(symbol);
		this.sqlSymbolManager = sqlSymbolManager;
	}
	
	AbstractSupportedSubQuerySQLSymbolParser(SQLSymbolManager sqlSymbolManager, String mappedOperator, String actualOperator){
		super(mappedOperator, actualOperator);
		this.sqlSymbolManager = sqlSymbolManager;
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
	@Override
	protected boolean subQuery(String field, String symbol, List paramlist, ParamValues paramValues, StringBuilder hql){
		if(LangUtils.isEmpty(paramlist)){
			return false;
		}
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
		SelectExtQuery subQuery = null;
		String subAlias = "sub_"+StringUtils.uncapitalize(subEntity.getSimpleName());
		if(paramlist.size()%2==0)
			subQuery = this.sqlSymbolManager.createSelectQuery(subEntity, subAlias, MyUtils.convertParamMap(paramlist.toArray()));
		else{
			paramlist.add(0, K.SELECT);//entity后第一个str为要select的字段
			subQuery = this.sqlSymbolManager.createSelectQuery(subEntity, subAlias, MyUtils.convertParamMap(paramlist.toArray()));
		}
		subQuery.setSubQuery(true);
		return subQuery;
	}
	

}
