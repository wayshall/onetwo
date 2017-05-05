package org.onetwo.common.utils;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;
import org.onetwo.common.date.DateUtils;

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
		
		String res = Integer.toString(35, 36);
		System.out.println("res:"+res);
		
		String date = DateUtils.formatDateTime(new Date(1481943274299L));
		System.out.println("date:"+date);
		
		double d = Double.NaN;
		System.out.println("nan: "+(Double.NaN==d));
		System.out.println("nan: "+(Double.valueOf(Double.NaN).equals(d)));
	}

}
