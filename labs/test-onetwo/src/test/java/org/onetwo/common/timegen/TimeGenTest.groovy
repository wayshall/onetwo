package org.onetwo.common.timegen;

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.onetwo.common.utils.DateRange
import org.onetwo.common.utils.timegen.DateOfMonthCycleGenerator
import org.onetwo.common.utils.timegen.DateOfWeekCycleGenerator
import org.onetwo.common.utils.timegen.ExactDateGenerator
import org.onetwo.common.utils.timegen.TimeRule
import org.onetwo.common.utils.timegen.Timegenerator
import org.onetwo.common.utils.timegen.TimeRule.RuleType

public class TimeGenTest {
	Timegenerator tg;
	@Before
	void before() {
		tg = new Timegenerator();
		tg.register(new ExactDateGenerator());
		tg.register(new DateOfWeekCycleGenerator());
		tg.register(new DateOfMonthCycleGenerator());
	}
	
	@Test
	public void testExactDate(){
		TimeRule rule = new TimeRule();
		rule.ruleType = RuleType.EXACT_DATE;
		rule.startTime = Date.parse("yyyy-MM-dd", "2015-01-01")
		rule.endTime = Date.parse("yyyy-MM-dd", "2015-01-03")
		List<DateRange> datelist = tg.generate(rule)
		Assert.assertEquals(1, datelist.size());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-01"), datelist[0].getStartNiceDate().getTime());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-03"), datelist[0].getEndNiceDate().getTime());
	}
	
	@Test
	public void testPeriodWeek(){
		//每周一
		TimeRule rule = new TimeRule();
		rule.ruleType = RuleType.PERIOD_WEEK;
		rule.startTime = Date.parse("yyyy-MM-dd", "2015-01-01")
		rule.endTime = Date.parse("yyyy-MM-dd", "2015-01-31")
		rule.startIndex = 1
		rule.endIndex = 1
		Collection<DateRange> dates = tg.generate(rule)
		List<DateRange> datelist = new ArrayList<DateRange>(dates);
		Assert.assertEquals(4, datelist.size());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-05"), datelist[0].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-05"), datelist[0].getEndDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-26"), datelist[3].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-26"), datelist[3].getEndDate());
		
		println "test=========="
		rule.startIndex = 4
		rule.endIndex = 7
		dates = tg.generate(rule)
		datelist = new ArrayList<DateRange>(dates);
		Assert.assertEquals(5, datelist.size());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-01"), datelist[0].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-04"), datelist[0].getEndDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-29"), datelist[4].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-31"), datelist[4].getEndDate());
	}
	
	
	@Test
	public void testPeriodMonth(){
		//每周一
		TimeRule rule = new TimeRule();
		rule.ruleType = RuleType.PERIOD_MONTH;
		rule.startTime = Date.parse("yyyy-MM-dd", "2015-01-22")
		rule.endTime = Date.parse("yyyy-MM-dd", "2015-12-10")
		rule.startIndex = 22
		rule.endIndex = 29
		Collection<DateRange> dates = tg.generate(rule)
		List<DateRange> datelist = new ArrayList<DateRange>(dates);
		Assert.assertEquals(11, datelist.size());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-22"), datelist[0].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-29"), datelist[0].getEndDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-02-22"), datelist[1].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-02-28"), datelist[1].getEndDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-11-22"), datelist[datelist.size()-1].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-11-29"), datelist[datelist.size()-1].getEndDate());
		
		println "test=========="
		rule.startIndex = 4
		rule.endIndex = 3
		dates = tg.generate(rule)
		datelist = new ArrayList<DateRange>(dates);
		Assert.assertEquals(0, datelist.size());
		
		println "test=========="
		rule.startIndex = 1
		rule.endIndex = 5
		dates = tg.generate(rule)
		datelist = new ArrayList<DateRange>(dates);
		Assert.assertEquals(11, datelist.size());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-02-01"), datelist[0].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-02-05"), datelist[0].getEndDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-03-01"), datelist[1].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-03-05"), datelist[1].getEndDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-12-01"), datelist[datelist.size()-1].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-12-05"), datelist[datelist.size()-1].getEndDate());
	}

}
