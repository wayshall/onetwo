package org.onetwo.groovy

import org.junit.Assert;
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

}
