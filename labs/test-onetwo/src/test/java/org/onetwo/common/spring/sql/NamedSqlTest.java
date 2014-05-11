package org.onetwo.common.spring.sql;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
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

}
