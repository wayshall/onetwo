package org.onetwo.common.utils;

import ognl.Ognl;
import ognl.OgnlException;

import org.junit.Assert;
import org.junit.Test;

public class OgnlTest {
	
	@Test
	public void test() throws OgnlException{
		Boolean val = (Boolean) Ognl.getValue("1!=2 and 2!=3", null);
		Assert.assertTrue(val);
		val = (Boolean) Ognl.getValue("1!=1 and 2!=3", null);
		Assert.assertFalse(val);
	}

}
