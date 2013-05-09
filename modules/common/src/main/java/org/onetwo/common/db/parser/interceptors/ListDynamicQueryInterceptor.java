package org.onetwo.common.db.parser.interceptors;

import java.util.List;

import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.db.parser.JFishConditon;
import org.onetwo.common.db.parser.QueryContext;
import org.onetwo.common.db.parser.SqlObject;
import org.onetwo.common.utils.LangUtils;

public class ListDynamicQueryInterceptor implements DynamicQueryInterceptor {
	
	private List<DynamicQueryInterceptor> interceptors = LangUtils.newArrayList();
	
	
	public ListDynamicQueryInterceptor(DynamicQueryInterceptor... interceptors) {
		this.addDynamicQueryInterceptor(interceptors);
	}
	
	final public ListDynamicQueryInterceptor addDynamicQueryInterceptor(DynamicQueryInterceptor... interceptors){
		for(DynamicQueryInterceptor dy : interceptors){
			if(dy!=null)
				this.interceptors.add(dy);
		}
		return this;
	}

	public void onParse(SqlObject sqlObject, List<JFishConditon> conditions){
		if(interceptors==null)
			return ;
		for(DynamicQueryInterceptor dy : interceptors){
			dy.onParse(sqlObject, conditions);
		}
	}
	
	public void onCompile(SqlObject sqlObject, JFishConditon cond, IfNull ifNull, StringBuilder segment, QueryContext qcontext){
		if(interceptors==null)
			return ;
		for(DynamicQueryInterceptor dy : interceptors){
			dy.onCompile(sqlObject, cond, ifNull, segment, qcontext);
		}
	}
	
	public String translateSql(String sql, QueryContext qcontext){
		if(interceptors==null)
			return sql;
		String newSql = sql;
		for(DynamicQueryInterceptor dy : interceptors){
			newSql = dy.translateSql(newSql, qcontext);
		}
		return newSql;
	}
	
	public String translateCountSql(String countSql, QueryContext qcontext){
		if(interceptors==null)
			return countSql;
		String newSql = countSql;
		for(DynamicQueryInterceptor dy : interceptors){
			newSql = dy.translateCountSql(newSql, qcontext);
		}
		return newSql;
	}

}
