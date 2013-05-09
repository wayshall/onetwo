package org.onetwo.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class VerySimpleStartMatcherTest {

	@Test
	public void testSimple(){
		String aa0 = "simple";
		String a0 = "Simple";
		String a1 = "Simple-bbb";
		String a2 = "aaa-Simple";
		String a3 = "aaa-Simple-bbb";
		VerySimpleStartMatcher vsm = VerySimpleStartMatcher.create("Simple*");
		Assert.assertTrue(vsm.match(aa0));
		Assert.assertTrue(vsm.match(a0));
		Assert.assertTrue(vsm.match(a1));
		Assert.assertFalse(vsm.match( a2));
		Assert.assertFalse(vsm.match( a3));
		
		vsm = VerySimpleStartMatcher.create("*Simple");
		Assert.assertTrue(vsm.match(aa0));
		Assert.assertTrue(vsm.match(a0));
		Assert.assertTrue(vsm.match(a2));
		Assert.assertFalse(vsm.match(a1));
		Assert.assertFalse(vsm.match(a3));
		
		vsm = VerySimpleStartMatcher.create("*Simple*");
		Assert.assertTrue(vsm.match(aa0));
		Assert.assertTrue(vsm.match(a0));
		Assert.assertTrue(vsm.match(a1));
		Assert.assertTrue(vsm.match(a2));
		Assert.assertTrue(vsm.match(a3));
		
		vsm = VerySimpleStartMatcher.create("*");
		Assert.assertTrue(vsm.match(aa0));
		Assert.assertTrue(vsm.match(a0));
		Assert.assertTrue(vsm.match(a1));
		Assert.assertTrue(vsm.match(a2));
		Assert.assertTrue(vsm.match(a3));
		
	}
	
	@Test
	public void testSimple2(){
		String aa0 = "public class";
		String a0 = "public class ";
		String a1 = "public class-bbb";
		String a2 = "aaa-public class";
		String a3 = "aaa-public class-bbb";
//		String[] strs = StringUtils.split("aaa : bb  ::  ccc  :::dd", ":");
		VerySimpleStartMatcher vsm = VerySimpleStartMatcher.createTokens(" ", "public", "class*");
		Assert.assertTrue(vsm.match(aa0));
		Assert.assertTrue(vsm.match(a0));
		Assert.assertTrue(vsm.match(a1));
		Assert.assertFalse(vsm.match( a2));
		Assert.assertFalse(vsm.match( a3));
		
		vsm = VerySimpleStartMatcher.createTokens(" ", "*public", "class");
		Assert.assertTrue(vsm.match(aa0));
		Assert.assertTrue(vsm.match(a0));
		Assert.assertTrue(vsm.match(a2));
		Assert.assertFalse(vsm.match(a1));
		Assert.assertFalse(vsm.match(a3));

		vsm = VerySimpleStartMatcher.createTokens(" ", "*public", "class*");
		Assert.assertTrue(vsm.match(aa0));
		Assert.assertTrue(vsm.match(a0));
		Assert.assertTrue(vsm.match(a1));
		Assert.assertTrue(vsm.match(a2));
		Assert.assertTrue(vsm.match(a3));
		
		vsm = VerySimpleStartMatcher.create("*");
		Assert.assertTrue(vsm.match(aa0));
		Assert.assertTrue(vsm.match(a0));
		Assert.assertTrue(vsm.match(a1));
		Assert.assertTrue(vsm.match(a2));
		Assert.assertTrue(vsm.match(a3));
		
	}

}
