package org.onetwo.common.jdbc;

import junit.framework.Assert;

import org.junit.Test;

public class JdbcUtilsTest {
	
	@Test
	public void testAccess(){
		DataBase db = JdbcUtils.getDataBase("jdbc:access:///d:/cardSync.mdb");
		Assert.assertEquals(DataBase.Access, db);
	}

}
