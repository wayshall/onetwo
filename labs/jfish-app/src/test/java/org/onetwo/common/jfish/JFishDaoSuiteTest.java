package org.onetwo.common.jfish;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	JFishDaoTest.class,
	JFishEntityManagerTest.class,
	JFishFileQueryDaoTest.class
})
public class JFishDaoSuiteTest {

}
