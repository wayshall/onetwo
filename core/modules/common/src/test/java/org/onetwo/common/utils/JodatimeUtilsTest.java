package org.onetwo.common.utils;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class JodatimeUtilsTest {

	@Test
	public void testDate(){
		DateTime date = JodatimeUtils.parse("2015-03-18");
		System.out.println("date: " + date.getDayOfMonth());
		Assert.assertEquals(18, date.getDayOfMonth());
		
		date = JodatimeUtils.parse("2016-04-13");
		System.out.println("week: " + date.getWeekOfWeekyear());
		Assert.assertEquals(15, date.getWeekOfWeekyear());
		
		DateTime dateTime = DateTime.parse("2016-04-13");
		System.out.println("dateTime:"+dateTime);
		DateTime start = dateTime.dayOfWeek().withMinimumValue();
		DateTime end = start.plusWeeks(1);
		System.out.println("start:"+start);
		System.out.println("end:"+end);
		Assert.assertEquals("2016-04-11", start.toString("yyyy-MM-dd"));
		Assert.assertEquals("2016-04-18", end.toString("yyyy-MM-dd"));


		start = dateTime.dayOfMonth().withMinimumValue();
		end = start.plusMonths(1);
		System.out.println("start:"+start);
		System.out.println("end:"+end);
		Assert.assertEquals("2016-04-01", start.toString("yyyy-MM-dd"));
		Assert.assertEquals("2016-05-01", end.toString("yyyy-MM-dd"));

		start = dateTime.dayOfYear().withMinimumValue();
		end = start.plusYears(1);
		System.out.println("start:"+start);
		System.out.println("end:"+end);
		Assert.assertEquals("2016-01-01", start.toString("yyyy-MM-dd"));
		Assert.assertEquals("2017-01-01", end.toString("yyyy-MM-dd"));
	}
}
