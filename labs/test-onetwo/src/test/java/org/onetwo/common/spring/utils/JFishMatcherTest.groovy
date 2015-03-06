package org.onetwo.common.spring.utils

import org.junit.Assert
import org.junit.Test

class JFishMatcherTest {
	
	@Test
	def void testMatcher(){
		JFishMathcer m = JFishMathcer.excludes(true, "*password*");
		Assert.assertTrue(m.match("testaaaa"))
		Assert.assertFalse(m.match("password"))
		Assert.assertFalse(m.match("userpassword"))
		Assert.assertTrue(m.match("userPassword"))
		
		m = JFishMathcer.excludes(false, "*password*");
		Assert.assertTrue(m.match("testaaaa"))
		Assert.assertFalse(m.match("password"))
		Assert.assertFalse(m.match("userpassword"))
		Assert.assertFalse(m.match("userPassword"))
		
		m = JFishMathcer.includes(false, "*password*");
		Assert.assertFalse(m.match("testaaaa"))
		Assert.assertTrue(m.match("password"))
		Assert.assertTrue(m.match("userpassword"))
		Assert.assertTrue(m.match("userPassword"))
	}

}
