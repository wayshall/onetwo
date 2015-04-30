package appweb.admin;


import org.onetwo.common.test.spring.SpringTxJUnitTestCase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

//@ActiveProfiles({ "test" })
@ContextConfiguration(loader=JFishAppContextLoaderForTest.class )
@TransactionConfiguration(defaultRollback = false)
public class AppBaseTest extends SpringTxJUnitTestCase {

}
