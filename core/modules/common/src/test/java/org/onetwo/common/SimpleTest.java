package org.onetwo.common;

import java.math.BigInteger;

import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.LangOps;

public class SimpleTest {
	
	@Test
	public void test(){
		String str = String.format( "(devType:%s^1.5)", 1 );
		System.out.println("str:"+str);
		int a = 1^5;
	}

}
