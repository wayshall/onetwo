package org.onetwo.common.utils;

import org.joda.time.DateTime;
import org.junit.Test;

public class SimpleTest {
	
	@Test
	public void test(){
		int val = 0xffff_ffff;
		System.out.println("val:"+val);
		short sval = (short)65535;
		System.out.println("sval:"+sval);
		
		DateTime now = DateTime.now();
		now = now.millisOfDay().withMinimumValue();
		System.out.println(now.toString("yyyy-MM-dd HH:mm:ss SSS"));
		now = now.plusDays(1);
		System.out.println(now.toString("yyyy-MM-dd HH:mm:ss SSS"));
	}

}
