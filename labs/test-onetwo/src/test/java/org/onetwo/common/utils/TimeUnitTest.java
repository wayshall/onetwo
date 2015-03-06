package org.onetwo.common.utils;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class TimeUnitTest {

	@Test
	public void test(){
		long t = TimeUnit.MILLISECONDS.toMinutes(60000);
		Assert.assertEquals(1, t);
		t = TimeUnit.MINUTES.toMillis(1);
		System.out.println("t: " + t);
		Assert.assertEquals(1000*60, t);
	}
}
