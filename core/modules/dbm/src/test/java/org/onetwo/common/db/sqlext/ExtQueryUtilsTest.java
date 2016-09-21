package org.onetwo.common.db.sqlext;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.onetwo.common.file.FileUtils;
import org.springframework.core.io.ClassPathResource;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcUtils;

public class ExtQueryUtilsTest {

	@Test
	public void testParseSql(){
		printStatements("select * from user where user_name=?");
		printStatements("select id, user_name, password from user where user_name=?");
		printStatements("select count(1) from user where user_name=?");
	}
	
	private void printStatements(String sql){
		List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcUtils.MYSQL);
		System.out.println("-------------------->size is:" + statements.size());
		for(SQLStatement stmt : statements){
			System.out.println(stmt.getClass()+": "+stmt);
			MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);
            
            //获取表名称
            System.out.println("Tables : " + visitor.getCurrentTable());
            //获取操作方法名称
            System.out.println("Tables : " + visitor.getTables());
            //获取字段名称
            System.out.println("fields : " + visitor.getColumns());
		}
		SQLSelectStatement selectStatement = (SQLSelectStatement)statements.get(0);
		SQLSelect select = selectStatement.getSelect();
		SQLSelectQueryBlock query = (SQLSelectQueryBlock)select.getQuery();
		List<SQLSelectItem> items = query.getSelectList();
		items.forEach(item->{
			System.out.println("SQLSelectItem:"+item.getExpr().getClass()+":"+item.getExpr());
			if(item.getExpr() instanceof SQLAggregateExpr){
				SQLAggregateExpr expr = (SQLAggregateExpr)item.getExpr();
				System.out.println("expr:"+expr);
			}else if(item.getExpr() instanceof SQLIdentifierExpr){
				SQLIdentifierExpr expr = (SQLIdentifierExpr)item.getExpr();
				System.out.println("identifier:"+expr);
			}
		});
		
		SQLExpr where = query.getWhere();
		System.out.println("where:"+where);
		
		SQLOrderBy orderby = query.getOrderBy();
		System.out.println("orderby:"+orderby);
		
	}
	

	private String readSql(String classPathFileName){
		String path = this.getClass().getPackage().getName().replace('.', '/')+"/"+classPathFileName;
		ClassPathResource cpr = new ClassPathResource(path);
		try {
			return FileUtils.readAsString(cpr.getFile());
		} catch (IOException e) {
			throw new RuntimeException("read  sql error!", e);
		}
	}
	@Test
	public void testOrderby2CountSql(){
		String sql = "select * from user where user_name=?";
		SQLSelectStatement selectStatement = this.changeAsCountSql(sql);
		System.out.println("new sql: "+selectStatement);

		//order by
		sql = readSql("testChangeSql.sql");
		printStatements(sql);
		selectStatement = this.changeAsCountSql(sql);
		System.out.println("new sql: "+selectStatement);
		
	}
	

	@Test
	public void testGroupUnion2CountSql(){
		//union group by
		String sql = readSql("testChangeSql-groupby.sql");
		printStatements(sql);
		SQLSelectStatement selectStatement = this.changeAsCountSql(sql);
		System.out.println("new sql: "+selectStatement);
	}
	
	private SQLSelectStatement changeAsCountSql(String sql){
		List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcUtils.MYSQL);
		SQLSelectStatement selectStatement = (SQLSelectStatement)statements.get(0);
		SQLSelect select = selectStatement.getSelect();
		SQLSelectQueryBlock query = (SQLSelectQueryBlock)select.getQuery();
		List<SQLSelectItem> items = query.getSelectList();
		items.clear();
		
		SQLSelectItem countItem = new SQLSelectItem();
		SQLAggregateExpr countMethod = new SQLAggregateExpr("count");
		countMethod.setParent(countItem);
		countMethod.addArgument(new SQLIntegerExpr(1));

		countItem.setParent(query);
		countItem.setExpr(countMethod);
		items.add(countItem);
		
		query.setOrderBy(null);
		
		return selectStatement;
	}
}
