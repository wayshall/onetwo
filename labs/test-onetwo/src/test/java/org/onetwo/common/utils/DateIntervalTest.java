package org.onetwo.common.utils;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.DateUtil.DateType;
import org.onetwo.common.utils.list.It;

public class DateIntervalTest {

	@Test
	public void testDateInterval(){
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
