package org.onetwo.common.spring.sql;

import java.util.List;

import org.junit.Test;
import org.onetwo.common.utils.ReflectUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;

public class NamedSqlTest {
	
	private String sql;
	
	@Test
	public void testSimpleNamedSql(){
		sql = "select * from admin_user t where t.userName=:userName and t.age=:age";
		ParsedSql psql = NamedParameterUtils.parseSqlStatement(sql);
		List<String> parameterNames = (List<String>) ReflectUtils.getFieldValue(psql, "parameterNames");
		for(String pname : parameterNames){
			System.out.println(pname);
		}
	}

}
