package org.onetwo.common.utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.LocalTime;
import org.joda.time.chrono.ISOChronology;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.date.DateRange;
import org.onetwo.common.date.DateRangeStaticFacotry;
import org.onetwo.common.date.DateUtil;
import org.onetwo.common.date.DateUtil.DateType;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.log.JFishLoggerFactory;

public class DateUtilTest {
	
	@Test
	public void testSqlTime(){
		Date now = new Date();
		Time time = new Time(now.getTime());
    	Time nowTime = new Time(now.getHours(), now.getMinutes(), now.getSeconds());
		String str = DateUtil.formatDateTime(time);
		System.out.println("time:"+str);
		System.out.println("time:"+now.getTime());
		System.out.println("time:"+time.getTime());
		System.out.println("nowTime:"+nowTime.getTime());
		
		LocalTime jodaTime = LocalTime.fromDateFields(now);
		System.out.println("jodaTime:"+jodaTime.toString());
		System.out.println("jodaTime:"+now.getTime());
		System.out.println("jodaTime:"+jodaTime.getMillisOfDay());
	}
	
	@Test
	public void testSimple(){
		Date date = new Date();
		System.out.println("testSimple: " + date);
		System.out.println("testSimple: " + String.valueOf(date.getTime()));
		System.out.println("testSimple: " + String.valueOf(date.getTime()).length());
		Date date2 = new Date("Mon Jun 08 16:54:44 CST 2015");
		System.out.println("date2: " + DateUtil.formatDateTime(date2));
	}
	
	@Test
	public void testMatch(){
		String dateStr = "1984-03-03";
		Assert.assertTrue(DateUtil.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtil.isYyyy(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "19840303";
		Assert.assertFalse(DateUtil.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtil.isYyyy(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984/03/03";
		Assert.assertTrue(DateUtil.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtil.isYyyy(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984-3-3";
		Assert.assertTrue(DateUtil.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtil.isYyyy(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm(dateStr));
		
		dateStr = "1984-03-43";
		Assert.assertFalse(DateUtil.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtil.isYyyy(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984-03-03 11";
		Assert.assertFalse(DateUtil.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM(dateStr));
		Assert.assertTrue(DateUtil.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm(dateStr));
		
		dateStr = "1984-03-03 11:11:11";
		Assert.assertFalse(DateUtil.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertTrue(DateUtil.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984-03-03 11:11";
		Assert.assertFalse(DateUtil.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH(dateStr));
		Assert.assertTrue(DateUtil.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984-03";
		Assert.assertFalse(DateUtil.isYyyy_MM_dd(dateStr));
		Assert.assertTrue(DateUtil.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtil.isYyyy(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984";
		Assert.assertFalse(DateUtil.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM(dateStr));
		Assert.assertTrue(DateUtil.isYyyy(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtil.isYyyy_MM_dd_HH_mm_ss(dateStr));
	}
	
	@Test
	public void testDate(){
		Date date = DateUtil.parseDate("2013-09-09");
		System.out.println("date: " + date.toLocaleString());
		Date date1 = NiceDate.New("2013-9-9").atTheBeginning().getTime();
		System.out.println("date1: " + date1.toLocaleString());
	}
	
	@Test
	public void testParse(){
		JFishLoggerFactory.getLogger(this.getClass()).info("test{}, test2{}", "aa", 1);
		Date test = new Date(1367078400000L);
		System.out.println("test: " + test.toLocaleString());
		
		/*Date now = DateUtil.date(":now");
		System.out.println("now: " + now.getTime())*/;
		
		Date today = DateUtil.date(":today");
		System.out.println("today: " + today);
		
		Date yesterday = DateUtil.date(":yesterday");
		System.out.println("yesterday: " + yesterday);
		
		Date tomorrow = DateUtil.date(":tomorrow");
		System.out.println("tomorrow: " + tomorrow);
		
		long t = TimeUnit.HOURS.toMillis(1);
		System.out.println("a hour millis : " + t);
		
		String checkInTime = "2012-08-31aaaaaaaaaaaaaaa";
		
		Date parseDate = DateUtil.parseByPatterns("20121029102501", "yyyyMMddHHmmss");
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
		
		newdate = DateUtil.parse("2012-09-01 22:22");
		Assert.assertNotNull(newdate);
		newdate = DateUtil.parse("22:22");
		Assert.assertNotNull(newdate);
		newdate = DateUtil.parse("2013-01-30");
		Assert.assertEquals("2013-1-30 0:00:00", newdate.toLocaleString());
		
		newdate = DateUtil.endOf(DateUtil.parse("2013-01-30"), DateType.date);
		Assert.assertEquals("2013-01-30 23:59:59.999", DateUtil.formatDateTimeMillis(newdate));
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
		Assert.assertFalse(DateUtil.isSameAt(start, end, DateType.date));
		
		start = DateUtil.asCalendar(DateUtil.parse("2013-5-08 21:30"));
		end = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30"));
		Assert.assertTrue(DateUtil.isSameAccurateAt(start, end, DateType.date));
		Assert.assertTrue(DateUtil.isSameAt(start, end, DateType.date));
		
		start = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30:32"));
		end = DateUtil.asCalendar(DateUtil.parse("2013-5-08 22:30:33"));
		Assert.assertTrue(DateUtil.isSameAccurateAt(start, end, DateType.year));
		Assert.assertTrue(DateUtil.isSameAccurateAt(start, end, DateType.date));
		Assert.assertTrue(DateUtil.isSameAccurateAt(start, end, DateType.hour));
		Assert.assertTrue(DateUtil.isSameAccurateAt(start, end, DateType.min));
		Assert.assertFalse(DateUtil.isSameAccurateAt(start, end, DateType.sec));
		
		Assert.assertTrue(DateUtil.isSameAt(start, end, DateType.year));
		Assert.assertTrue(DateUtil.isSameAt(start, end, DateType.date));
		Assert.assertTrue(DateUtil.isSameAt(start, end, DateType.hour));
		Assert.assertTrue(DateUtil.isSameAt(start, end, DateType.min));
		Assert.assertFalse(DateUtil.isSameAt(start, end, DateType.sec));
		

		Date date1 = DateUtil.parse("2014-11-30 22:30:32");
		Date date2 = DateUtil.parse("2014-12-01 22:30:32");
		Assert.assertFalse(DateUtil.isSameAt(date1, date2, DateType.date));
		
		date1 = DateUtil.parse("2014-12-31 22:30:32");
		date2 = DateUtil.parse("2015-01-01 22:30:32");
		Assert.assertFalse(DateUtil.isSameAt(date1, date2, DateType.date));
		
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
	
	@Test
	public void testSplitWeek(){
		Date startDate = DateUtil.parse("2015-01-01");
		Date endDate = DateUtil.parse("2015-01-31");
		Collection<DateRange> dateRange = DateRangeStaticFacotry.splitAsDateRangeByWeek(startDate, endDate);
		List<DateRange> dateList = new ArrayList<DateRange>(dateRange);
		
		Assert.assertEquals(5, dateList.size());
		Assert.assertEquals(DateUtil.parse("2015-01-01"), dateList.get(0).getStartDate());
		Assert.assertEquals(DateUtil.parse("2015-01-04"), dateList.get(0).getEndDate());
		Assert.assertEquals(DateUtil.parse("2015-01-19"), dateList.get(3).getStartDate());
		Assert.assertEquals(DateUtil.parse("2015-01-25"), dateList.get(3).getEndDate());
		Assert.assertEquals(DateUtil.parse("2015-01-26"), dateList.get(4).getStartDate());
		Assert.assertEquals(DateUtil.parse("2015-01-31"), dateList.get(4).getEndDate());
	}
	
	@Test
	public void testSplitMonth(){
		Date startDate = DateUtil.parse("2015-01-25");
		Date endDate = DateUtil.parse("2015-12-25");
		Collection<DateRange> dateRange = DateRangeStaticFacotry.splitAsDateRangeByMonth(startDate, endDate);
		List<DateRange> dateList = new ArrayList<DateRange>(dateRange);

		Assert.assertEquals(12, dateList.size());
		Assert.assertEquals(DateUtil.parse("2015-01-25"), dateList.get(0).getStartDate());
		Assert.assertEquals(DateUtil.parse("2015-01-31"), dateList.get(0).getEndDate());
		Assert.assertEquals(DateUtil.parse("2015-02-01"), dateList.get(1).getStartDate());
		Assert.assertEquals(DateUtil.parse("2015-02-28"), dateList.get(1).getEndDate());
		Assert.assertEquals(DateUtil.parse("2015-09-01"), dateList.get(8).getStartDate());
		Assert.assertEquals(DateUtil.parse("2015-09-30"), dateList.get(8).getEndDate());
		Assert.assertEquals(DateUtil.parse("2015-12-01"), dateList.get(11).getStartDate());
		Assert.assertEquals(DateUtil.parse("2015-12-25"), dateList.get(11).getEndDate());
	}
	
	
	public static void main(String[] args){
	}

}
