package org.onetwo.common.utils;

import org.junit.Test;

public class PrimeBasicGeneraorTest {
	
	PrimeBasicGeneraor g = PrimeBasicGeneraor.generatedMaxNumber(9999);
	
	@Test
	public void testCount(){
		int count = g.getPrimeCount();
		System.out.println("count: " + count);
//		System.out.println("g: " + g);
	}

}
