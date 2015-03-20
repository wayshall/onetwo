package org.onetwo.common.utils;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class JodatimeUtilsTest {

	@Test
	public void testDate(){
		DateTime date = JodatimeUtils.parse("2015-03-18");
		System.out.println("date: " + date.getDayOfMonth());
		Assert.assertEquals(18, date.getDayOfMonth());
	}
}
