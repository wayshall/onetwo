package org.onetwo.app;



import org.onetwo.common.test.spring.SpringBaseJUnitTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

//@ActiveProfiles({ "test" })
//@ContextConfiguration(value="classpath:/applicationContext-test.xml")
@ContextConfiguration(loader=DBTestLoader.class)
@TransactionConfiguration(defaultRollback = false)
public class AppBaseTest extends SpringBaseJUnitTestCase {

}
