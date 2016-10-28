package org.onetwo.common.spring.retry;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(classes=RetryTestContext.class)
public class RetryTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private RetryTestService retryTestService;
	
	@Test
	public void testRetrySomething(){
		int count = this.retryTestService.saySomething();
		Assert.assertEquals(3, count);
	}

}
