package org.onetwo.common.db.parser.interceptors;

import java.util.List;

import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.db.parser.JFishConditonImpl;
import org.onetwo.common.db.parser.QueryContext;
import org.onetwo.common.db.parser.QueryContext.QValue;
import org.onetwo.common.db.parser.SqlCondition;
import org.onetwo.common.db.parser.SqlKeywords;
import org.onetwo.common.db.parser.SqlObject;
import org.onetwo.common.db.parser.SqlVarObject;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class DefaultDynamicQueryInterceptor extends DynamicQueryInterceptorAdaptor {

	public static final String[] SQL_KEY_WORKDS = new String[]{" ", "-", "'", ";", ",", "(", ")"};
	private static final String REPLACE_SQL = "1=1 ";

	
	@Override
	public void onParse(SqlObject sqlObj, List<SqlCondition> conditions){
		if(SqlVarObject.class.isInstance(sqlObj)){
			JFishConditonImpl cond = new JFishConditonImpl((SqlVarObject)sqlObj, conditions.size());
			conditions.add(cond);
		}
	}
	
	@Override
	public void onCompile(SqlObject sqlObj, SqlCondition cond, IfNull ifNull, StringBuilder segmentBuf, QueryContext qcontext){
		String segment = "";
		if (cond!=null) {
			if(IfNull.Ignore==ifNull){
				if(cond.isAvailable())
					segment = cond.toSqlString();
				else
					segment = REPLACE_SQL;
			}else{
				if(cond.isAvailable())
					segment = cond.toSqlString();
				else
					LangUtils.throwBaseException("the parameter["+cond.getVarname()+"] has not value");
			}
		}else{
			segment = sqlObj.toFragmentSql();
		}
		segmentBuf.append(segment);
	}

	
	@Override
	public String translateSql(String sql, QueryContext qcontext){
		List<QValue<String, String[]>> orderBys = qcontext.orderBy();
		if(LangUtils.isEmpty(orderBys)){
			return sql;
		}
		StringBuilder orderByStr = new StringBuilder();
		int index = 0;
		for(QValue<String, String[]> ob : orderBys){
			if(index!=0)
				orderByStr.append(", ");
			
//			orderByStr.append(StringUtils.join(ob.value, ", ")).append(" ").append(ob.key);
			for(int i=0; i<ob.value.length; i++){
				if(i!=0)
					orderByStr.append(", ");
				checkFieldNameValid(ob.value[i]);
				orderByStr.append(ob.value[i]);
			}
			orderByStr.append(" ").append(ob.key);
			
			index++;
		}
		if(index!=0)
			sql += " order by " + orderByStr;
			
		return sql;
	}


	protected void checkFieldNameValid(String field){
		Assert.hasText(field);
		/*for(String str : SQL_KEY_WORKDS){
			if(field.indexOf(str)!=-1)
				LangUtils.throwBaseException("the field is inValid : " + field);
		}*/
		if(SqlKeywords.ALL.isKeyWord(field.trim())){
			throw new BaseException("the field is inValid : " + field);
		}
	}
	
	@Override
	public String translateCountSql(String countSql, QueryContext qcontext){
		return buildCountSql(countSql, "");
	}


	public static String buildCountSql(String sql, String countValue){
		String countField = "*";
		String hql = StringUtils.substringAfter(sql, "from ");
		if(StringUtils.isBlank(hql)){
			hql = StringUtils.substringAfter(sql, "FROM ");
		}

		if(StringUtils.isNotBlank(countValue))
			countField = countValue;
		
		hql = "select count(" + countField + ") from " + hql;
		return hql;
	}
}
