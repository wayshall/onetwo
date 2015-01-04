package org.onetwo.common.timegen;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test
import org.onetwo.common.timegen.TimeRule.RuleType

public class TimeGenTest {
	Timegenerator tg;
	@Before
	void before() {
		tg = new Timegenerator();
		tg.register(new ExactDateGenerator());
		tg.register(new DateCycleGenerator());
	}
	
	@Test
	public void testExactDate(){
		TimeRule rule = new TimeRule();
		rule.ruleType = RuleType.EXACT_DATE;
		rule.startTime = Date.parse("yyyy-MM-dd", "2015-01-01")
		rule.endTime = Date.parse("yyyy-MM-dd", "2015-01-03")
		List<DateRange> datelist = tg.generate(rule)
		Assert.assertEquals(3, datelist.size());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-01"), datelist[0].getStartDate());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-03"), datelist[0].getEndDate());
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
		List<Date> datelist = tg.generate(rule)
		Assert.assertEquals(4, datelist.size());
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-05"), datelist[0]);
		Assert.assertEquals(Date.parse("yyyy-MM-dd", "2015-01-26"), datelist[3]);
	}

}
