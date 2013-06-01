package org.onetwo.common.utils;

import java.util.Date;

import org.junit.Test;

public class NiceDateTest {

	@Test
	public void testThisDate(){
		NiceDate now = new NiceDate();
		now.setTime(new Date());
		now.thisDate();
		now.reset();
		now.thisHour();
		System.out.println("date: " + now);
		now.yesterday();
		System.out.println("date: " + now);
	}

	@Test
	public void testDateModify(){
		NiceDate now = NiceDate.New();
		Date today = now.getTime();
		System.out.println("today: " + today.toLocaleString());
		now.yesterday();
		System.out.println("yesterday: " + now.getTime().toLocaleString());
		System.out.println("today: " + today.toLocaleString());
	}

	@Test
	public void testInTimes(){
		NiceDate now = NiceDate.New().setMillis(1352277656381L);
		System.out.println("m: "+now.toString());
		now = NiceDate.New().setTimeInSeconds(1351353600);
		System.out.println("m: "+now.toString());
	}
}
