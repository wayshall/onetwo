package org.onetwo.common.db;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.dbm.exception.DbmException;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.util.JdbcUtils;

abstract public class DruidUtils {
	

	public static String toCountSql(String sql, Object value){
		return changeAsCountStatement(sql, value).toString();
	}

	private static SQLSelectStatement changeAsCountStatement(String sql, Object value){
		List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcUtils.MYSQL);
		if(!SQLSelectStatement.class.isInstance(statements.get(0))){
			throw new DbmException("it must be a select query, sql: " + sql);
		}
		SQLSelectStatement selectStatement = (SQLSelectStatement)statements.get(0);
		SQLSelect select = selectStatement.getSelect();
		SQLSelectQueryBlock query = (SQLSelectQueryBlock)select.getQuery();
		List<SQLSelectItem> items = query.getSelectList();
		items.clear();
		
		SQLSelectItem countItem = new SQLSelectItem();
		SQLAggregateExpr countMethod = new SQLAggregateExpr("count");
		countMethod.setParent(countItem);
		if(value==null || value instanceof Number || StringUtils.isBlank(value.toString())){
			countMethod.addArgument(new SQLIntegerExpr(1));
		}else{
			countMethod.addArgument(new SQLIdentifierExpr(value.toString()));
		}

		countItem.setParent(query);
		countItem.setExpr(countMethod);
		items.add(countItem);
		
		query.setOrderBy(null);
		
		return selectStatement;
	}
	
	private DruidUtils(){
	}

}
