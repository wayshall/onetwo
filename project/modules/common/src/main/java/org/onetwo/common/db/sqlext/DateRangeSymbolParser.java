package org.onetwo.common.db.sqlext;

import java.util.Date;
import java.util.List;

import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.db.ParamValues;
import org.onetwo.common.db.QueryField;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.DateUtil;

/****
 * 对in操作符的解释
 * 
 * @author weishao
 *
 */
public class DateRangeSymbolParser extends CommonSQLSymbolParser implements HqlSymbolParser {
	
	public DateRangeSymbolParser(SQLSymbolManager sqlSymbolManager){
		super(sqlSymbolManager, "date-in");
	}
	
	@SuppressWarnings("rawtypes")
	public String parse(String symbol, QueryField context){

		String field = context.getActualFieldName();
		Object value = context.getValue();
		ParamValues paramValues = context.getExtQuery().getParamsValue();
		IfNull ifNull = context.getExtQuery().getIfNull();
		
		/*if(value==null || (value instanceof String && StringUtils.isBlank(value.toString())))
			return null;
		
		List paramlist = MyUtils.asList(value);
		if(paramlist==null || paramlist.isEmpty())
			return null;*/
		
		List paramlist = ExtQueryUtils.processValue(field, value, ifNull);

		if(paramlist.size()>2)
			throw new ServiceException("the parameters of "+symbol+" can not greater than 2, acutal: " + paramlist.size());
		
		Date startDate = null;
		Date endDate = null;
		
		try {
			if(paramlist.size()==1){
				Date date = getDate(paramlist.get(0));
				startDate = DateUtil.setDateStart(date);
				endDate = DateUtil.setDateEnd(date);
			}else{
				startDate = getDate(paramlist.get(0));
				endDate = getDate(paramlist.get(1));
			}
		} catch (ClassCastException e) {
			throw new ServiceException("the parameter type of "+symbol+" is error, check it!", e);
		}
		
		field = this.getFieldName(field);
		StringBuilder hql = new StringBuilder();

		if(!this.subQuery(field, symbol, paramlist, paramValues, hql)){
			hql.append("( ");
			hql.append(field).append(" >= ");
			paramValues.addValue(field, startDate, hql);
			hql.append(" and ").append(field).append(" < ");
			paramValues.addValue(field, endDate, hql);
			hql.append(" ) ");
			return hql.toString();
		}
		
		return hql.toString();
	}
	
	protected Date getDate(Object value){
		Date date = null;
		if(value instanceof String){
			date = DateUtil.date(value.toString());
		}else{
			date = (Date)value;
		}
		return date;
	}
	
}
