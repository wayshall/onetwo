package org.onetwo.common.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.date.DateUtils;

public class JodatimeUtilsTest {
	
	@Test
	public void testBetween(){
		Date start = DateUtils.parse("2016-12-06 17:35:30");
		Date end = DateUtils.parse("2016-12-06 17:35:33");
		Period period = JodatimeUtils.between(start, end);
		assertThat(period.getSeconds(), equalTo(Seconds.THREE.getSeconds()));
		assertThat(Seconds.secondsBetween(new LocalDateTime(start), new LocalDateTime(end)), equalTo(Seconds.THREE));
	}

	@Test
	public void testDate(){
		/*LocalDateTime fromDate = new LocalDateTime(new Date());
		String str = fromDate.toString("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println("str:"+str);
		LocalTime localTime = fromDate.toLocalTime();
		System.out.println("localTime:"+localTime.toString());
		System.out.println("localTime:"+localTime.toString("yyyy-MM-dd HH:mm:ss"));
		System.out.println("toDateTimeToday:"+localTime.toDateTimeToday().toDate().toLocaleString());
		
		fromDate = new LocalDateTime(new Date());
		fromDate = fromDate.year().setCopy(1970).monthOfYear().setCopy(1).dayOfMonth().setCopy(1);
		System.out.println("fromDate:"+fromDate.toString("yyyy-MM-dd HH:mm:ss"));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(1970, 0, 1);
		System.out.println("cal:"+cal.getTime().toLocaleString());*/
		
		DateTime dt = DateTime.now().millisOfDay().withMinimumValue();;
		System.out.println(JodatimeUtils.formatDateTime(dt.toDate()));
		
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
	
	@Test
	public void testInterval(){
		DateTime start = JodatimeUtils.parse("2016-11-24");
		DateTime end = JodatimeUtils.parse("2016-12-24");
		Interval interval = new Interval(start, end);
		boolean res = interval.contains(start.plusSeconds(1));
		Assertions.assertThat(res).isTrue();
		res = interval.contains(start.minusSeconds(1));
		Assertions.assertThat(res).isFalse();
	}
}
