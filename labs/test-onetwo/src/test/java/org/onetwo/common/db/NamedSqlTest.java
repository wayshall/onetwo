package org.onetwo.common.db;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.spring.sql.SqlParamterPostfixFunctions;
import org.onetwo.common.spring.sql.SqlUtils;
import org.onetwo.common.spring.sql.SqlUtils.ParsedSqlWrapper;
import org.onetwo.common.spring.sql.SqlUtils.ParsedSqlWrapper.SqlParamterMeta;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;

public class NamedSqlTest {
	
	private String sql;
	
	@Test
	public void testSimpleNamedSql(){
		sql = "select * from admin_user t where t.userName=:userName and (t.age=:user.age or t.age=:2) and t.idcode=?";
		ParsedSql psql = NamedParameterUtils.parseSqlStatement(sql);
		List<String> parameterNames = (List<String>) ReflectUtils.getFieldValue(psql, "parameterNames");
		for(String pname : parameterNames){
			System.out.println(pname);
		}
		Assert.assertEquals("userName", parameterNames.get(0));
		Assert.assertEquals("user.age", parameterNames.get(1));
		List<int[]> parameterIndexs = (List<int[]>) ReflectUtils.getFieldValue(psql, "parameterIndexes");
		for(int[] index : parameterIndexs){
			System.out.println(LangUtils.toString(index));
		}
	}
	
	@Test
	public void testSimpleNamedSql2(){
		sql = "select * from admin_user t where t.userName=:user.userName?likString and (t.age=:user.age or t.age=:2) and t.idcode=?";
		ParsedSql psql = NamedParameterUtils.parseSqlStatement(sql);
		List<String> parameterNames = (List<String>) ReflectUtils.getFieldValue(psql, "parameterNames");
		for(String pname : parameterNames){
			System.out.println(pname);
		}
		Assert.assertEquals("user.userName?likString", parameterNames.get(0));
		Assert.assertEquals("user.age", parameterNames.get(1));
		List<int[]> parameterIndexs = (List<int[]>) ReflectUtils.getFieldValue(psql, "parameterIndexes");
		for(int[] index : parameterIndexs){
			System.out.println(LangUtils.toString(index));
		}
	}
	
	@Test
	public void testSqlUtils(){
		sql = "select * from admin_user t where (t.userName=:user.userName?like or t.userName=:user.userName?prelike or t.userName=:user.userName?postlike) and (t.age=:user.age or t.age=:2) and t.idcode=?";
		ParsedSqlWrapper sqlWrapper = SqlUtils.parseSql(sql);
		for(SqlParamterMeta parameter : sqlWrapper.getParameters()){
			System.out.println("name: " + parameter);
			if("like".equals(parameter.getFunction())){
				Object val = ReflectUtils.invokeMethod(parameter.getFunction(), SqlParamterPostfixFunctions.getInstance(), "way");
				System.out.println("val:"+ val);
				Assert.assertEquals("%way%", val);
			}
			if("prelike".equals(parameter.getFunction())){
				Object val = ReflectUtils.invokeMethod(parameter.getFunction(), SqlParamterPostfixFunctions.getInstance(), "way");
				System.out.println("val:"+ val);
				Assert.assertEquals("%way", val);
			}
			if("postlike".equals(parameter.getFunction())){
				Object val = ReflectUtils.invokeMethod(parameter.getFunction(), SqlParamterPostfixFunctions.getInstance(), "way");
				System.out.println("val:"+ val);
				Assert.assertEquals("way%", val);
			}
		}
	}

}
