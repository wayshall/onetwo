package org.onetwo.enumtest;

import junit.framework.Assert;

import org.junit.Test;

public class EnumTest {

	@Test
	public void testTestLib(){
		TestLib juit = TestLib.valueOf("JUNIT");
		Assert.assertEquals(TestLib.JUNIT, juit);
	}
}
