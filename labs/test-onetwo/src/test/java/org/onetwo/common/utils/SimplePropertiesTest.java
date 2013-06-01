package org.onetwo.common.utils;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.propconf.PropUtils;
import org.onetwo.common.utils.propconf.SimpleProperties;

public class SimplePropertiesTest {
	
	private SimpleProperties prop = PropUtils.loadAsSimpleProperties("simple.properties");
	
	@Test
	public void test(){
		String val = prop.getProperty("debug");
		Assert.assertEquals("true", val);

		//user.name = username:{0} and {1}
		val = prop.getProperty("user.name", "way1", "way2");
		Assert.assertEquals("username:way1 and way2", val);
		
		val = prop.getPropertyWithDefault("user.name111", "test");
		Assert.assertEquals("test", val);	
		
		val = prop.getPropertyWithDefault("user.name111", "username is {0}", "test");
		Assert.assertEquals("username is test", val);

		boolean b = prop.getPropertyAsType("boolean", boolean.class);
		Assert.assertTrue(b);
		b = prop.getPropertyAsType("boolean.false", boolean.class);
		Assert.assertFalse(b);

		int number = prop.getPropertyAsType("number", int.class);
		Assert.assertTrue(number==1111);
		

		//user.name = username:{0} and {1}
		val = prop.parseNamed("user.name.parser", "name1", "way1", "name2", "way2");
		Assert.assertEquals("username:way1 and way2", val);
	}

}
