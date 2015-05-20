package org.onetwo.jodatime;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.DateUtil.DateType;
import org.onetwo.common.utils.JodatimeUtils;

public class JodatimeTest {
	
	@Test
	public void test(){
		DateTime date1 = JodatimeUtils.parse("2015-03-09 11:11:11 111", DateUtil.DATE_TIME_MILLS2);
		Date date2 = DateUtil.parseByPatterns("2015-03-09 11:11:11 111", DateUtil.DATE_TIME_MILLS2);
		Assert.assertEquals(date1.toDate(), date2);
		
		DateTime startDate1 = JodatimeUtils.atStartOfDate(date1.toDate());
		Date startDate2 = DateUtil.beginningOf(date2, DateType.date);
		Assert.assertEquals(startDate1.toDate(), startDate2);
		
		DateTime endDate1 = JodatimeUtils.atEndOfDate(date1.toDate());
		Date endDate2 = DateUtil.endOf(date2, DateType.date);
		Assert.assertEquals(endDate1.toDate(), endDate2);
	}

}
