package org.onetwo.common.utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.date.DateRange;
import org.onetwo.common.date.DateRangeStaticFacotry;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.date.DateUtils.DateType;
import org.onetwo.common.date.Dates;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.log.JFishLoggerFactory;

public class DateUtilTest {
	
	@Test
	public void testSqlTime(){
		Date now = new Date();
		Time time = new Time(now.getTime());
    	Time nowTime = new Time(now.getHours(), now.getMinutes(), now.getSeconds());
		String str = DateUtils.formatDateTime(time);
		System.out.println("time:"+DateUtils.format("h:mm a", new Date()));
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
		System.out.println("date2: " + DateUtils.formatDateTime(date2));
	}
	
	@Test
	public void testMatch(){
		String dateStr = "1984-03-03";
		Assert.assertTrue(DateUtils.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtils.isYyyy(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "19840303";
		Assert.assertFalse(DateUtils.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtils.isYyyy(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984/03/03";
		Assert.assertTrue(DateUtils.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtils.isYyyy(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984-3-3";
		Assert.assertTrue(DateUtils.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtils.isYyyy(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm(dateStr));
		
		dateStr = "1984-03-43";
		Assert.assertFalse(DateUtils.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtils.isYyyy(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984-03-03 11";
		Assert.assertFalse(DateUtils.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM(dateStr));
		Assert.assertTrue(DateUtils.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm(dateStr));
		
		dateStr = "1984-03-03 11:11:11";
		Assert.assertFalse(DateUtils.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertTrue(DateUtils.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984-03-03 11:11";
		Assert.assertFalse(DateUtils.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH(dateStr));
		Assert.assertTrue(DateUtils.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984-03";
		Assert.assertFalse(DateUtils.isYyyy_MM_dd(dateStr));
		Assert.assertTrue(DateUtils.isYyyy_MM(dateStr));
		Assert.assertFalse(DateUtils.isYyyy(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm_ss(dateStr));
		
		dateStr = "1984";
		Assert.assertFalse(DateUtils.isYyyy_MM_dd(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM(dateStr));
		Assert.assertTrue(DateUtils.isYyyy(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm(dateStr));
		Assert.assertFalse(DateUtils.isYyyy_MM_dd_HH_mm_ss(dateStr));
	}
	
	@Test
	public void testDate(){
		Date date = DateUtils.parseDate("2013-09-09");
		System.out.println("date: " + date.toLocaleString());
		Date date1 = NiceDate.New("2013-9-9").atTheBeginning().getTime();
		System.out.println("date1: " + date1.toLocaleString());
		
		try {
			date = new Date(Date.parse("2013-09-09"));
			Assert.fail();
		} catch (Exception e) {
			Assert.assertNotNull(e);;
		}
		

    	LocalDateTime deliverAt = LocalDateTime.now().plusDays(1)
    										.withHour(12)
    										.withMinute(0)
    										.withSecond(0);
    	System.out.println("deliverAt: " + deliverAt.format(Dates.DATE_TIME));
	}
	
	@Test
	public void testParse(){
		JFishLoggerFactory.getLogger(this.getClass()).info("test{}, test2{}", "aa", 1);
		Date test = new Date(1367078400000L);
		System.out.println("test: " + test.toLocaleString());
		
		/*Date now = DateUtil.date(":now");
		System.out.println("now: " + now.getTime())*/;
		
		Date today = DateUtils.date(":today");
		System.out.println("today: " + today);
		
		Date yesterday = DateUtils.date(":yesterday");
		System.out.println("yesterday: " + yesterday);
		
		Date tomorrow = DateUtils.date(":tomorrow");
		System.out.println("tomorrow: " + tomorrow);
		
		long t = TimeUnit.HOURS.toMillis(1);
		System.out.println("a hour millis : " + t);
		
		String checkInTime = "2012-08-31aaaaaaaaaaaaaaa";
		
		Date parseDate = DateUtils.parseByPatterns("20121029102501", "yyyyMMddHHmmss");
		System.out.println("parseDate:" + parseDate.toLocaleString());
		

		Calendar cal = Calendar.getInstance();
		System.out.println("day: " + cal.get(Calendar.DAY_OF_WEEK));
	}
	
	@Test
	public void testDateParse(){
		Date date = DateUtils.parse("2012-09-09 22:22:22");
		Assert.assertNotNull(date);
		
		Date bdate = DateUtils.beginningOf(date, DateType.month);
		Assert.assertEquals("2012-09-01 00:00:00", DateUtils.formatDateTime(bdate));
		
		bdate = DateUtils.endOf(date, DateType.month);
		Assert.assertEquals("2012-09-30 23:59:59", DateUtils.formatDateTime(bdate));
		bdate = DateUtils.beginningOf(date, DateType.hour);
		Assert.assertEquals("2012-09-09 22:00:00", DateUtils.formatDateTime(bdate));
		bdate = DateUtils.endOf(date, DateType.hour);
		Assert.assertEquals("2012-09-09 22:59:59", DateUtils.formatDateTime(bdate));
		
		
		Date newdate = DateUtils.parse("2012-09-09 22:22");
		Assert.assertNotNull(newdate);
		
		newdate = DateUtils.parseDateShortTime("2012-09-09 22:22:22");
		Assert.assertNotNull(newdate);
		
		newdate = DateUtils.parseDateTime("2012-09-09 22:22:22");
		Assert.assertEquals(date.getTime(), newdate.getTime());
		
		newdate = DateUtils.parse("2012-09-01 22:22");
		Assert.assertNotNull(newdate);
		newdate = DateUtils.parse("22:22");
		Assert.assertNotNull(newdate);
		newdate = DateUtils.parse("2013-01-30");
		Assert.assertEquals("2013-1-30 0:00:00", newdate.toLocaleString());
		
		newdate = DateUtils.endOf(DateUtils.parse("2013-01-30"), DateType.date);
		Assert.assertEquals("2013-01-30 23:59:59.999", DateUtils.formatDateTimeMillis(newdate));
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
		Calendar start = DateUtils.asCalendar(DateUtils.parse("2012-5-08 22:30"));
		Calendar end = DateUtils.asCalendar(DateUtils.parse("2013-5-08 22:30"));
		Assert.assertFalse(DateUtils.isSameAccurateAt(start, end, DateType.date));
		Assert.assertFalse(DateUtils.isSameAt(start, end, DateType.date));
		
		start = DateUtils.asCalendar(DateUtils.parse("2013-5-08 21:30"));
		end = DateUtils.asCalendar(DateUtils.parse("2013-5-08 22:30"));
		Assert.assertTrue(DateUtils.isSameAccurateAt(start, end, DateType.date));
		Assert.assertTrue(DateUtils.isSameAt(start, end, DateType.date));
		
		start = DateUtils.asCalendar(DateUtils.parse("2013-5-08 22:30:32"));
		end = DateUtils.asCalendar(DateUtils.parse("2013-5-08 22:30:33"));
		Assert.assertTrue(DateUtils.isSameAccurateAt(start, end, DateType.year));
		Assert.assertTrue(DateUtils.isSameAccurateAt(start, end, DateType.date));
		Assert.assertTrue(DateUtils.isSameAccurateAt(start, end, DateType.hour));
		Assert.assertTrue(DateUtils.isSameAccurateAt(start, end, DateType.min));
		Assert.assertFalse(DateUtils.isSameAccurateAt(start, end, DateType.sec));
		
		Assert.assertTrue(DateUtils.isSameAt(start, end, DateType.year));
		Assert.assertTrue(DateUtils.isSameAt(start, end, DateType.date));
		Assert.assertTrue(DateUtils.isSameAt(start, end, DateType.hour));
		Assert.assertTrue(DateUtils.isSameAt(start, end, DateType.min));
		Assert.assertFalse(DateUtils.isSameAt(start, end, DateType.sec));
		

		Date date1 = DateUtils.parse("2014-11-30 22:30:32");
		Date date2 = DateUtils.parse("2014-12-01 22:30:32");
		Assert.assertFalse(DateUtils.isSameAt(date1, date2, DateType.date));
		
		date1 = DateUtils.parse("2014-12-31 22:30:32");
		date2 = DateUtils.parse("2015-01-01 22:30:32");
		Assert.assertFalse(DateUtils.isSameAt(date1, date2, DateType.date));
		
	}
	
	@Test
	public void testCompareAccurateAt(){
		Calendar start = DateUtils.asCalendar(DateUtils.parse("2012-5-08 22:30"));
		Calendar end = DateUtils.asCalendar(DateUtils.parse("2013-5-08 22:30"));
		Assert.assertEquals(-1, DateUtils.compareAccurateAt(start, end, DateType.date));
		Assert.assertEquals(1, DateUtils.compareAccurateAt(end, start, DateType.date));
		
		start = DateUtils.asCalendar(DateUtils.parse("2013-5-08 21:30"));
		end = DateUtils.asCalendar(DateUtils.parse("2013-5-08 22:30"));
		Assert.assertEquals(0, DateUtils.compareAccurateAt(start, end, DateType.date));
		
		start = DateUtils.asCalendar(DateUtils.parse("2013-5-08 22:30:32"));
		end = DateUtils.asCalendar(DateUtils.parse("2013-5-08 22:30:33"));
		Assert.assertEquals(0, DateUtils.compareAccurateAt(start, end, DateType.year));
		Assert.assertEquals(0, DateUtils.compareAccurateAt(start, end, DateType.date));
		Assert.assertEquals(0, DateUtils.compareAccurateAt(start, end, DateType.hour));
		Assert.assertEquals(0, DateUtils.compareAccurateAt(start, end, DateType.min));
		Assert.assertEquals(-1, DateUtils.compareAccurateAt(start, end, DateType.sec));
	}
	
	@Test
	public void testSplitWeek(){
		Date startDate = DateUtils.parse("2015-01-01");
		Date endDate = DateUtils.parse("2015-01-31");
		Collection<DateRange> dateRange = DateRangeStaticFacotry.splitAsDateRangeByWeek(startDate, endDate);
		List<DateRange> dateList = new ArrayList<DateRange>(dateRange);
		
		Assert.assertEquals(5, dateList.size());
		Assert.assertEquals(DateUtils.parse("2015-01-01"), dateList.get(0).getStartDate());
		Assert.assertEquals(DateUtils.parse("2015-01-04"), dateList.get(0).getEndDate());
		Assert.assertEquals(DateUtils.parse("2015-01-19"), dateList.get(3).getStartDate());
		Assert.assertEquals(DateUtils.parse("2015-01-25"), dateList.get(3).getEndDate());
		Assert.assertEquals(DateUtils.parse("2015-01-26"), dateList.get(4).getStartDate());
		Assert.assertEquals(DateUtils.parse("2015-01-31"), dateList.get(4).getEndDate());
	}
	
	@Test
	public void testSplitMonth(){
		Date startDate = DateUtils.parse("2015-01-25");
		Date endDate = DateUtils.parse("2015-12-25");
		Collection<DateRange> dateRange = DateRangeStaticFacotry.splitAsDateRangeByMonth(startDate, endDate);
		List<DateRange> dateList = new ArrayList<DateRange>(dateRange);

		Assert.assertEquals(12, dateList.size());
		Assert.assertEquals(DateUtils.parse("2015-01-25"), dateList.get(0).getStartDate());
		Assert.assertEquals(DateUtils.parse("2015-01-31"), dateList.get(0).getEndDate());
		Assert.assertEquals(DateUtils.parse("2015-02-01"), dateList.get(1).getStartDate());
		Assert.assertEquals(DateUtils.parse("2015-02-28"), dateList.get(1).getEndDate());
		Assert.assertEquals(DateUtils.parse("2015-09-01"), dateList.get(8).getStartDate());
		Assert.assertEquals(DateUtils.parse("2015-09-30"), dateList.get(8).getEndDate());
		Assert.assertEquals(DateUtils.parse("2015-12-01"), dateList.get(11).getStartDate());
		Assert.assertEquals(DateUtils.parse("2015-12-25"), dateList.get(11).getEndDate());
	}
	
	
	public static void main(String[] args){
	}

}
