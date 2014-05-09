package org.onetwo.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class RandUtilsTest {
	
	@Test
	public void testRandomInt(){
		for (int i = 0; i < 100; i++) {
			int r = RandUtils.randomInt(5);
//			System.out.println(r);
			Assert.assertTrue(r>=0 && r<5);
		}
	}
	
	@Test
	public void testRandomWithPadLeft(){
		for (int i = 0; i < 100; i++) {
			String str = RandUtils.randomWithPadLeft(999, "0");
			Assert.assertTrue(str.length()==3);
		}
		
	}

}
