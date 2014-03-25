package org.onetwo;

import java.util.Date;

import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.DateUtil.DateType;





public class Test {
	
	public class Parent {
		public static final String s1 = "s1";
	}
	
	public class Child extends Parent {
//		public static final String s1 = "c1";
	}
	
	public static void main(String[] args){
		String date = "2014-01-19";
		Date monthDate = DateUtil.parseByPatterns(date, "yyyy-MM");
		monthDate = DateUtil.beginningOf(monthDate, DateType.month);
		String startDate = DateUtil.formatDate(monthDate);
		System.out.println("startDate: " + startDate);
		
		monthDate = DateUtil.endOf(monthDate, DateType.month);
		String endDate = DateUtil.formatDate(monthDate);
		System.out.println("endDate: " + endDate);
	}
	
}
