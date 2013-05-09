package org.onetwo.common.db;

import org.junit.Assert;
import org.junit.Test;

public class SQLFunctionTemplateTest {
	
	@Test
	public void testFunc(){
		String rs = DefaultSQLFunctionManager.get().exec("lower", "field");
		Assert.assertEquals("lower(field)", rs);
		
		rs = DefaultSQLFunctionManager.get().exec("substring", "field", 5, 3);
		Assert.assertEquals("substring(field, 5, 3)", rs);
	}

}
