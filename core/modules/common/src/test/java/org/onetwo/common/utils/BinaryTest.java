package org.onetwo.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class BinaryTest {
	
	@Test
	public void testBinaryToOct(){
		String bin = "01";
		int val = Integer.valueOf(bin, 2);
		Assert.assertEquals(1, val);
		bin = "11";
		val = Integer.valueOf(bin, 2);
		Assert.assertEquals(3, val);
	}
	
	@Test
	public void testOctToBinary(){
		int val = 10;
		String bin = Integer.toBinaryString(val);
		Assert.assertEquals("1010", bin);
		val = 4;
		bin = Integer.toBinaryString(val);
		Assert.assertEquals("100", bin);
	}

}
