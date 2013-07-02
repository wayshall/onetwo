package org.onetwo.common.db.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JFishSqlParserTest {
	
	JFishSqlParser parser;
	
	@Before
	public void setup(){
	}

	@Test
	public void testSimpleSql(){
		String sql = "select count ( * ) from t_user t where t.age = ? and t.birth_day = :birth_day and t.user_name = :userName";
		parser = new JFishSqlParser(sql);
		SqlStatment statment = parser.parse();
		/*for(SqlObjectImpl sqlobj : statment.getSqlObjects()){
			System.out.println("SqlObjectImpl:"+sqlobj.toFragmentSql());
		}*/
		System.out.println("testSimpleSql:"+statment.toSql());
		String excepted = "select count( * ) from t_user t where t.age = ? and t.birth_day = :birth_day and t.user_name = :userName";
		Assert.assertEquals(excepted, statment.toSql());
	}
	@Test
	public void testSimpleOrderby(){
		String sql = "select count( * ) from t_user t where t.age = ? and t.birth_day = :birth_day order by t.id , t.user_name desc";
		parser = new JFishSqlParser(sql);
		SqlStatment statment = parser.parse();
		/*for(SqlObjectImpl sqlobj : statment.getSqlObjects()){
			System.out.println("SqlObjectImpl:"+sqlobj.toFragmentSql());
		}
		System.out.println("sql:"+statment.toSql());*/
		String excepted = "select count( * ) from t_user t where t.age = ? and t.birth_day = :birth_day order by t.id, t.user_name desc";
		Assert.assertEquals(excepted, statment.toSql());
	}
	@Test
	public void testLeftVar(){
		String sql = "select count( * ) from t_user t where ? = 77 and t.birth_day = :birth_day and :userName like '%way%'";
		parser = new JFishSqlParser(sql);
		SqlStatment statment = parser.parse();
		for(SqlObject sqlobj : statment.getSqlObjects()){
			System.out.println("SqlObjectImpl:"+sqlobj.toFragmentSql());
		}
		System.out.println("sql:"+statment.toSql());

		String excepted = "select count( * ) from t_user t where ? = 77 and t.birth_day = :birth_day and :userName like '%way%'";
		Assert.assertEquals(excepted, statment.toSql());
	}
	@Test
	public void testSqlFunction(){
		String sql = "select count( * ) from t_user t where to_date ( t.birth_day , 'yyyy-MM-dd' ) >= :birth_day and t.user_name = substring ( :user_name , 3 , 2 )";
		parser = new JFishSqlParser(sql);
		SqlStatment statment = parser.parse();
		int varIndex=0;
		for(SqlObject sqlobj : statment.getSqlObjects()){
			System.out.println("varIndex:" + varIndex + ", obj: " + sqlobj);
			if(varIndex==5){
				SqlInfixVarConditionExpr infix = (SqlInfixVarConditionExpr) sqlobj;
				Assert.assertEquals("birth_day", infix.getVarname());
				Assert.assertEquals(">=", infix.getOperator().getName());
			}else if(varIndex==8){
				SqlInfixVarConditionExpr infix = (SqlInfixVarConditionExpr) sqlobj;
				Assert.assertEquals("user_name", infix.getVarname());
				Assert.assertEquals("=", infix.getOperator().getName());
			}
			varIndex++;
		}
//		System.out.println("sql:"+statment.toSql());
		String excepted = "select count( * ) from t_user t where to_date( t.birth_day, 'yyyy-MM-dd' ) >= :birth_day and t.user_name = substring( :user_name, 3, 2 )";
		Assert.assertEquals(excepted, statment.toSql());
	}
	@Test
	public void testSqlWithSub(){
		String sql = "select count( * ) from t_user t where to_date ( t.birth_day , 'yyyy-MM-dd' ) >= :birth_day and t.user_name in ( select tt.user_name from t_user tt )";
		parser = new JFishSqlParser(sql);
		SqlStatment statment = parser.parse();
		int varIndex=0;
		for(SqlObject sqlobj : statment.getSqlObjects()){
			System.out.println("varIndex:" + varIndex + ", obj: " + sqlobj);
			if(varIndex==5){
				SqlInfixVarConditionExpr infix = (SqlInfixVarConditionExpr) sqlobj;
				Assert.assertEquals("birth_day", infix.getVarname());
			}/*else if(varIndex==7){
				SqlInfixVarConditionExpr infix = (SqlInfixVarConditionExpr) sqlobj;
				Assert.assertEquals("user_name", infix.getVarname());
			}*/
			varIndex++;
		}
		System.out.println("sql:"+statment.toSql());
		String excepted = "select count( * ) from t_user t where to_date( t.birth_day, 'yyyy-MM-dd' ) >= :birth_day and t.user_name in ( select tt.user_name from t_user tt )";
		Assert.assertEquals(excepted, statment.toSql());
	}

}
