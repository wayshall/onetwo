package org.example.app;


import org.onetwo.common.jfish.JFishAppContextLoaderForTest;
import org.onetwo.common.test.spring.SpringTxJUnitTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

//@ActiveProfiles({ "test" })
@ContextConfiguration(loader=JFishAppContextLoaderForTest.class )
@TransactionConfiguration(defaultRollback = false)
public class AppBaseTest extends SpringTxJUnitTestCase {

}
