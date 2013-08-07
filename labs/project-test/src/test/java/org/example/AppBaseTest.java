package org.example;


import org.onetwo.common.test.spring.SpringTxJUnitTestCase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ActiveProfiles({ "test" })
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class AppBaseTest extends SpringTxJUnitTestCase {

}
