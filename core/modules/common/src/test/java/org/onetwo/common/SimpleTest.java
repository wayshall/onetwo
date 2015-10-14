package org.onetwo.common;

import java.math.BigInteger;

import org.junit.Test;

public class SimpleTest {
	
	@Test
	public void test(){
		String milis = "014";
		BigInteger time = new BigInteger("13164449"+milis);
		time = time.multiply(new BigInteger("20"));
		time = time.add(new BigInteger(milis));
		time = time.mod(new BigInteger("3000"));
		System.out.println("time:" + time);
	}

}
