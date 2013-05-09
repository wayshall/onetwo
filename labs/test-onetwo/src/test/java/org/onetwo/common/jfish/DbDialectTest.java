package org.onetwo.common.jfish;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.fish.orm.AbstractDBDialect.DBMeta;

public class DbDialectTest {
	
	@Test
	public void testName(){
		DBMeta dm = new DBMeta();
		dm.setDbName("mysql");
		Assert.assertTrue(dm.isMySQL());
		Assert.assertFalse(dm.isOracle());
	}
	

}
