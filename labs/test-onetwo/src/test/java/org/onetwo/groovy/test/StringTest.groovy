package org.onetwo.groovy.test

import org.junit.Assert
import org.junit.Test

class StringTest {
	
	@Test
	def void testSplit(){
		String str = "aa;bb";
		println(str.split(';'))
		Assert.assertEquals("[aa, bb]", str.split(';').toString());
		
		str = "";
		println(str.split(';'))
		Assert.assertEquals("[]", str.split(';').toString());
		
		String str2;
		str2 = str2==null?"":str2.split(';')
		println(str2.split(';'))
		Assert.assertEquals("[]", str2.split(';').toString());
	}
	
	@Test
	def void testToDate(){
		def str = "2014-12-30"
		Date date = Date.parse("yyyy-MM-dd", str)
		println("date: ${date.format('yyyy-MM-dd')}")
		Assert.assertEquals(str, date.format("yyyy-MM-dd"))
	}

}
