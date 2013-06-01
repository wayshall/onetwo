package org.onetwo.common.db.parser.interceptors;

import java.util.List;

import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.db.parser.JFishConditon;
import org.onetwo.common.db.parser.QueryContext;
import org.onetwo.common.db.parser.SqlObject;

public class DynamicQueryInterceptorAdaptor implements DynamicQueryInterceptor {
	
	public void onParse(SqlObject sqlObject, List<JFishConditon> conditions){
		
	}
	
	public void onCompile(SqlObject sqlObject, JFishConditon cond, IfNull ifNull, StringBuilder segment, QueryContext qcontext){
		
	}
	
	public String translateSql(String sql, QueryContext qcontext){
		return sql;
	}
	
	public String translateCountSql(String countSql, QueryContext qcontext){
		return countSql;
	}

}
