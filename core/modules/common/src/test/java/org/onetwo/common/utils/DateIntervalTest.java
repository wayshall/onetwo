package org.onetwo.common.utils;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.date.DateInterval;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.date.DateUtils.DateType;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.utils.list.It;

public class DateIntervalTest {
	
	@Test
	public void testSplit(){
		DateInterval interval = DateInterval.in("2014-09-01", "2014-09-11");
		List<Date> dates = interval.splitDate();
		Assert.assertEquals(11, dates.size());
		Assert.assertEquals("2014-09-01", NiceDate.New(dates.get(0)).formatAsDate());
		Assert.assertEquals("2014-09-11", NiceDate.New(dates.get(dates.size()-1)).formatAsDate());
		
		interval = DateInterval.in("2014-09-15", "2014-10-11");//16+11
		dates = interval.splitDate();
		int index = 0;
		for(Date date : dates){
			System.out.println("testSplit date["+(index++)+"]: " + DateUtils.formatDate(date));
		}
		Assert.assertEquals(27, dates.size());
		Assert.assertEquals("2014-09-15", NiceDate.New(dates.get(0)).formatAsDate());
		Assert.assertEquals("2014-10-11", NiceDate.New(dates.get(dates.size()-1)).formatAsDate());
		
		interval = DateInterval.in("2014-09-15", "2014-10-11");
		List<Date> months = interval.splitMonth();
		Assert.assertEquals(2, months.size());
		Assert.assertEquals("2014-09", NiceDate.New(months.get(0)).format("yyyy-MM"));
		
		/*interval = DateInterval.in("2015-01-0", "2015-01-31");
		List<Date> months = interval.splitMonth();
		Assert.assertEquals(2, months.size());
		Assert.assertEquals("2014-09", NiceDate.New(months.get(0)).format("yyyy-MM"));*/
	}

	@Test
	public void testDateInterval(){
		DateInterval interval = DateInterval.in("2014-09-01", "2014-09-11");
		List<String> dates = interval.getInterval(DateType.date).format("yyyy-MM-dd");
		Assert.assertEquals("[2014-09-01, 2014-09-02, 2014-09-03, 2014-09-04, 2014-09-05, 2014-09-06, 2014-09-07, 2014-09-08, 2014-09-09, 2014-09-10]", dates.toString());
		
		dates = interval.getInterval(DateType.month, 1, true).format("yyyyMM");
		Assert.assertEquals("[201409]", dates.toString());
		

		interval = DateInterval.in("2014-06-01", "2014-09-11");
		dates = interval.getInterval(DateType.month, 1, true).format("yyyyMM");
		System.out.println("month:" + dates);
		Assert.assertEquals("[201406, 201407, 201408, 201409]", dates.toString());
	}

	@Test
	public void testDateInterval2(){
		DateInterval interval = DateInterval.in("2012-11-11 16:50:50", "2013-5-28 16:50:50");
		final long differ = interval.getStandardDays();
		System.out.println("differ: " + differ);
		interval.eachDate(new It<Date>() {

			@Override
			public boolean doIt(Date element, int index) {
				System.out.println(index+": " + element.toLocaleString());
				Assert.assertTrue(index<=differ);
				return true;
			}

		});
		
		interval = DateInterval.in("2013-05-11 23:50:50", "2013-5-15 16:50:50");
		List<Date> list = interval.getIntervalByDate(1, false);
		System.out.println("data1: " + NiceDate.New(list.get(0)).formatAsDateTime());
		Assert.assertEquals(4, list.size());
		list = interval.getIntervalByDate(1, true);
		Assert.assertEquals(5, list.size());

		list = interval.getIntervalByDate(2, false);
		Assert.assertEquals(2, list.size());

		list = interval.getIntervalByDate(2, true);
		Assert.assertEquals(3, list.size());
		
		Assert.assertEquals(1, interval.getInterval(DateType.year, 1, true).size());
		Assert.assertEquals(0, interval.getInterval(DateType.year).size());
		

		interval = DateInterval.in("2010-05-11 23:50:50", "2013-5-15 16:50:50");
		List<Date> intervalDates = interval.getInterval(DateType.year, 1, true);
		System.out.println("intervalDates: " +LangUtils.toString(intervalDates));
		
		Assert.assertEquals(4, intervalDates.size());
		intervalDates = interval.getInterval(DateType.year);
		Assert.assertEquals(3, intervalDates.size());
		

		interval = DateInterval.in("2010-05-11 23:50:50", "2013-5-15 16:50:50");
		intervalDates = interval.getInterval(DateType.year, 2, true);
		System.out.println("intervalDates: " +LangUtils.toString(intervalDates));
		Assert.assertEquals(2, intervalDates.size());
	}
	
}
