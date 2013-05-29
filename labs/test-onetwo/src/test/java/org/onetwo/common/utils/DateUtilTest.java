package org.onetwo.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.DateUtil.DateType;

public class DateUtilTest {
	
	@Test
	public void testParse(){
		MyLoggerFactory.getLogger(this.getClass()).info("test{}, test2{}", "aa", 1);
		Date test = new Date(1367078400000L);
		System.out.println("test: " + test.toLocaleString());
		
		Date now = DateUtil.date(":now");
		System.out.println("now: " + now.getTime());
		
		Date today = DateUtil.date(":today");
		System.out.println("today: " + today);
		
		Date yesterday = DateUtil.date(":yesterday");
		System.out.println("yesterday: " + yesterday);
		
		Date tomorrow = DateUtil.date(":tomorrow");
		System.out.println("tomorrow: " + tomorrow);
		
		long t = TimeUnit.HOURS.toMillis(1);
		System.out.println("a hour millis : " + t);
		
		String checkInTime = "2012-08-31aaaaaaaaaaaaaaa";
		System.out.println("aa:"+DateUtil.parse(checkInTime , DateUtil.YYYY_MM_DD).toLocaleString());
		
		Date parseDate = DateUtil.parse("20121029102501", "yyyyMMddHHmmss");
		System.out.println("parseDate:" + parseDate.toLocaleString());
		

		Calendar cal = Calendar.getInstance();
		System.out.println("day: " + cal.get(Calendar.DAY_OF_WEEK));
	}
	
	@Test
	public void testDateParse(){
		Date date = DateUtil.parse("2012-09-09 22:22:22");
		Assert.assertNotNull(date);
		
		Date bdate = DateUtil.beginningOf(date, DateType.month);
		Assert.assertEquals("2012-09-01 00:00:00", DateUtil.formatDateTime(bdate));
		
		bdate = DateUtil.endOf(date, DateType.month);
		Assert.assertEquals("2012-09-30 23:59:59", DateUtil.formatDateTime(bdate));
		bdate = DateUtil.beginningOf(date, DateType.hour);
		Assert.assertEquals("2012-09-09 22:00:00", DateUtil.formatDateTime(bdate));
		bdate = DateUtil.endOf(date, DateType.hour);
		Assert.assertEquals("2012-09-09 22:59:59", DateUtil.formatDateTime(bdate));
		
		
		Date newdate = DateUtil.parse("2012-09-09 22:22");
		Assert.assertNotNull(newdate);
		
		newdate = DateUtil.parseDateShortTime("2012-09-09 22:22:22");
		Assert.assertNotNull(newdate);
		
		newdate = DateUtil.parseDateTime("2012-09-09 22:22:22");
		Assert.assertEquals(date.getTime(), newdate.getTime());
		
		newdate = DateUtil.parse("2012-09 22:22");
		Assert.assertNotNull(newdate);
		newdate = DateUtil.parse("22:22");
		Assert.assertNotNull(newdate);
		newdate = DateUtil.parse("2013-01-30");
		Assert.assertEquals("2013-1-30 0:00:00", newdate.toLocaleString());
	}
	
	@Test
	public void testDateString(){
		for(long i=0; i<10000l; i++){
			String str = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
//			System.out.println(str.length()+":"+str);
		}

		for(long i=0; i<10000l; i++){
			String str = String.valueOf(System.nanoTime());
//			System.out.println(str.length()+":"+str);
		}
	}

	@Test
	public void testIsSameAccurateAt(){
		Calendar start = DateUtil.asCalendar(DateUtil.parse("2012-5-08 22:30"));
		Calendar end = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30"));
		Assert.assertFalse(DateUtil.isSameAccurateAt(start, end, DateType.date));
		
		start = DateUtil.asCalendar(DateUtil.parse("2013-5-08 21:30"));
		end = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30"));
		Assert.assertTrue(DateUtil.isSameAccurateAt(start, end, DateType.date));
		
		start = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30:32"));
		end = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30:33"));
		Assert.assertTrue(DateUtil.isSameAccurateAt(start, end, DateType.year));
		Assert.assertTrue(DateUtil.isSameAccurateAt(start, end, DateType.date));
		Assert.assertTrue(DateUtil.isSameAccurateAt(start, end, DateType.hour));
		Assert.assertTrue(DateUtil.isSameAccurateAt(start, end, DateType.min));
		Assert.assertFalse(DateUtil.isSameAccurateAt(start, end, DateType.sec));
	}
	@Test
	public void testCompareAccurateAt(){
		Calendar start = DateUtil.asCalendar(DateUtil.parse("2012-5-08 22:30"));
		Calendar end = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30"));
		Assert.assertEquals(-1, DateUtil.compareAccurateAt(start, end, DateType.date));
		Assert.assertEquals(1, DateUtil.compareAccurateAt(end, start, DateType.date));
		
		start = DateUtil.asCalendar(DateUtil.parse("2013-5-08 21:30"));
		end = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30"));
		Assert.assertEquals(0, DateUtil.compareAccurateAt(start, end, DateType.date));
		
		start = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30:32"));
		end = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30:33"));
		Assert.assertEquals(0, DateUtil.compareAccurateAt(start, end, DateType.year));
		Assert.assertEquals(0, DateUtil.compareAccurateAt(start, end, DateType.date));
		Assert.assertEquals(0, DateUtil.compareAccurateAt(start, end, DateType.hour));
		Assert.assertEquals(0, DateUtil.compareAccurateAt(start, end, DateType.min));
		Assert.assertEquals(-1, DateUtil.compareAccurateAt(start, end, DateType.sec));
	}
	
	public static void main(String[] args){
	}

}
