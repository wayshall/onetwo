package org.onetwo.common.utils;

import org.junit.Test;
import org.junit.Assert;

public class BooleanTest {
	
	@Test
	public void testBooleans(){
		//false if any one false
		Assert.assertTrue(true & true);
		Assert.assertTrue(true & true & true);
		Assert.assertFalse(true & true & false);
		Assert.assertFalse(true & true & false & true);
		Assert.assertFalse(true & true & false & true & false);
		Assert.assertFalse(true & true & false & false);
	}

}
