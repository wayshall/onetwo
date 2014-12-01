package org.onetwo.common.quartz;

import java.text.ParseException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.NiceDate;
import org.quartz.CronExpression;

public class CronTest {
	
	@Test
	public void testSimple() throws ParseException{
		/*CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setCronExpression("0/30 * * * * ?");*/
		Date date = DateUtil.parse("2014-09-25 00:00:00");
		
		CronExpression cronExp = new CronExpression("0/30 * * * * ?");
		Date cdate1 = cronExp.getTimeAfter(date);
		Date cdate2 = cronExp.getNextValidTimeAfter(date);
		Assert.assertEquals(cdate1, cdate2);;
		Assert.assertEquals("2014-09-25 00:00:30", NiceDate.New(cdate1).formatAsDateTime());
		

		cronExp = new CronExpression("0 0 12 11 11 ?");
		boolean invid = cronExp.isSatisfiedBy(date);
		Assert.assertFalse(invid);
		date = DateUtil.parse("2014-11-11 12:00:00");
		invid = cronExp.isSatisfiedBy(date);
		Assert.assertTrue(invid);
		

//		cronExp = new CronExpression("0/1 * 9:20-8:30 * * ?");
		cronExp = new CronExpression("0 0/20 9-7 * * ?");
		date = DateUtil.parse("2014-11-14 09:00:00");
		invid = cronExp.isSatisfiedBy(date);
		Assert.assertTrue(invid);
		date = DateUtil.parse("2014-11-14 16:00:00");
		invid = cronExp.isSatisfiedBy(date);
		Assert.assertTrue(invid);
		date = DateUtil.parse("2014-11-14 02:00:00");
		invid = cronExp.isSatisfiedBy(date);
		Assert.assertTrue(invid);
		
		date = DateUtil.parse("2014-11-14 08:00:00");
		invid = cronExp.isSatisfiedBy(date);
		Assert.assertFalse(invid);
		date = DateUtil.parse("2014-11-14 08:40:00");
		invid = cronExp.isSatisfiedBy(date);
		Assert.assertFalse(invid);
		date = DateUtil.parse("2014-11-14 08:20:00");
		invid = cronExp.isSatisfiedBy(date);
		Assert.assertFalse(invid);
		date = DateUtil.parse("2014-11-14 09:15:00");
		invid = cronExp.isSatisfiedBy(date);
		Assert.assertFalse(invid);
		

		cronExp = new CronExpression("0 0/20 6-8,11-1 * * ?");
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 07:00:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 07:20:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 07:40:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 11:00:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 11:20:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 11:40:00"));
		Assert.assertTrue(invid);


		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 0:00:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 0:40:00"));
		Assert.assertTrue(invid);

		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 1:00:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 1:40:00"));
		Assert.assertTrue(invid);

		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 6:00:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 6:40:00"));
		Assert.assertTrue(invid);

		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 09:00:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 09:10:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 10:00:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 10:20:00"));
		Assert.assertFalse(invid);


		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 2:00:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 2:40:00"));
		Assert.assertFalse(invid);

		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 4:00:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 4:40:00"));
		Assert.assertFalse(invid);
		


		cronExp = new CronExpression("0 0/20 11-1 * * ?");
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 07:00:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 07:20:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 07:40:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 10:40:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 11:00:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 11:20:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 11:40:00"));
		Assert.assertTrue(invid);


		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 0:00:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 0:40:00"));
		Assert.assertTrue(invid);

		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 1:00:00"));
		Assert.assertTrue(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 1:40:00"));
		Assert.assertTrue(invid);

		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 6:00:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 6:40:00"));
		Assert.assertFalse(invid);

		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 09:00:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 09:10:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 10:00:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 10:20:00"));
		Assert.assertFalse(invid);


		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 2:00:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 2:40:00"));
		Assert.assertFalse(invid);

		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 4:00:00"));
		Assert.assertFalse(invid);
		invid = cronExp.isSatisfiedBy(DateUtil.parse("2014-11-14 4:40:00"));
		Assert.assertFalse(invid);
	}

}
