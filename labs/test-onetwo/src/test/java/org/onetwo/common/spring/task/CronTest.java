package org.onetwo.common.spring.task;

import java.text.ParseException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.NiceDate;
import org.springframework.scheduling.support.CronSequenceGenerator;

public class CronTest {

	@Test
	public void testSimple() throws ParseException{
		/*CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setCronExpression("0/30 * * * * ?");*/
		Date date = DateUtil.parse("2014-09-25 00:00:00");
		
		CronSequenceGenerator cronExp = new CronSequenceGenerator("0/30 * * * * ?");
		Date cdate1 = cronExp.next(date);
		Assert.assertEquals("2014-09-25 00:00:30", NiceDate.New(cdate1).formatAsDateTime());
		

		cronExp = new CronSequenceGenerator("0 0 12 11 11 ?");
		Date validDate = cronExp.next(DateUtil.now());
		date = DateUtil.parse("2014-11-11 12:00:00");
		Assert.assertEquals(date, validDate);
	}
}
