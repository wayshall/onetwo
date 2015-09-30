package org.onetwo.common.utils;

import org.junit.Test;
import org.junit.Assert;

public class BooleanTest {
	
	@Test
	public void testAnd(){
		//false if any one false
		Assert.assertTrue(true & true);
		Assert.assertTrue(true & true & true);
		Assert.assertFalse(true & true & false);
		Assert.assertFalse(true & true & false & true);
		Assert.assertFalse(true & true & false & true & false);
		Assert.assertFalse(true & true & false & false);
	}
	
	@Test
	public void testOr(){
		//false if any one false
		Assert.assertTrue(true | true);
		Assert.assertTrue(true | true | true);
		Assert.assertFalse(false | false | false);
		Assert.assertTrue(false | true | false | true);
		Assert.assertTrue(false | true | false | true | false);
		Assert.assertTrue(true | true | false | false);
	}

}
