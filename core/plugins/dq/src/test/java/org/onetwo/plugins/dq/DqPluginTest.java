package org.onetwo.plugins.dq;

import org.junit.Test;
import org.onetwo.common.test.spring.SpringBaseJUnitTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(loader=JFishAppContextLoaderForTest.class )
@TransactionConfiguration(defaultRollback = false)
public class DqPluginTest extends SpringBaseJUnitTestCase {

	@Test
	public void test(){
		System.out.println("Test");
	}
}
