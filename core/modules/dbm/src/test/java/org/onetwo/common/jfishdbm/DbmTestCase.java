package org.onetwo.common.jfishdbm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	JFishDaoTest.class,
	JFishEntityManagerTest.class,
//	OneBatchInsertTest.class,
	BatchInsertTest.class
})
public class DbmTestCase {

}
