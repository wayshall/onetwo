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
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.util.JdbcUtils;

abstract public class DruidUtils {

	public static String toCountSql(String sql, Object value){
		return changeAsCountStatement(sql, value).toString();
	}

	/*public static String toCountSql2(String sql, Object value){
		return changeAsCountStatement2(sql, value).toString();
	}

	public static SQLSelectStatement changeAsCountStatement2(String sql, Object value){
		List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcUtils.MYSQL);
		SQLSelectStatement selectStatement = getSQLSelectStatement(statements, 0);
		if(selectStatement==null){
			throw new DbmException("it must be a select query, sql: " + sql);
		}
		selectStatement.accept(new TrimOrderBySQLASTVisitorAdapter());
		return selectStatement;
	}*/
	public static SQLSelectStatement getSQLSelectStatement(List<SQLStatement> statements, int index){
		if(!SQLSelectStatement.class.isInstance(statements.get(index))){
			return null;
		}
		SQLSelectStatement selectStatement = (SQLSelectStatement)statements.get(index);
		return selectStatement;
	}
	public static SQLSelectStatement changeAsCountStatement(String sql, Object value){
		List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcUtils.MYSQL);
		SQLSelectStatement selectStatement = getSQLSelectStatement(statements, 0);
		if(selectStatement==null){
			throw new DbmException("it must be a select query, sql: " + sql);
		}
		SQLSelect select = selectStatement.getSelect();
		SQLSelectQueryBlock query = (SQLSelectQueryBlock)select.getQuery();

//		query.setOrderBy(null);
		selectStatement.accept(new TrimOrderBySQLASTVisitorAdapter());
		if(query.getGroupBy()!=null){
			SQLSelectQueryBlock countquery = new SQLSelectQueryBlock();	
			SQLSelectStatement countSql = new SQLSelectStatement(new SQLSelect(countquery), JdbcUtils.MYSQL);
			
			SQLSelectItem countItem = createCountSelectForQuery(countquery, "");
			
			SQLSubqueryTableSource sub = new SQLSubqueryTableSource(select);
			sub.setAlias("countView");
			
			countquery.addSelectItem(countItem);
			countquery.setFrom(sub);

			return countSql;
		}
		
		List<SQLSelectItem> items = query.getSelectList();
		items.clear();
		
		SQLSelectItem countItem = createCountSelectForQuery(query, "");
		items.add(countItem);

		
		return selectStatement;
	}

	protected static SQLSelectItem createCountSelectForQuery(SQLSelectQuery query, Object value){
		SQLSelectItem countItem = new SQLSelectItem();
		SQLAggregateExpr countMethod = new SQLAggregateExpr("count");
		countMethod.setParent(countItem);
		if(value==null || value instanceof Number || StringUtils.isBlank(value.toString())){
			countMethod.addArgument(new SQLIntegerExpr(1));
		}else{
			countMethod.addArgument(new SQLIdentifierExpr(value.toString()));
		}
		
		countItem.setExpr(countMethod);
		countItem.setParent(query);
		
		return countItem;
	}
	
	static class TrimOrderBySQLASTVisitorAdapter extends MySqlASTVisitorAdapter {
	    @Override
	    public void endVisit(MySqlSelectQueryBlock x) {
	    	x.setOrderBy(null);
	    }
	}
	private DruidUtils(){
	}

}
