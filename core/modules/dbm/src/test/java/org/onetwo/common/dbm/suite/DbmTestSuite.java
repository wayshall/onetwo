package org.onetwo.common.dbm.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.onetwo.common.dbm.BatchInsertTest;
import org.onetwo.common.dbm.DBCheckerTest;
import org.onetwo.common.dbm.model.service.UserAutoidServiceTest;

@RunWith(Suite.class)
@SuiteClasses({DBCheckerTest.class, UserAutoidServiceTest.class, BatchInsertTest.class})
public class DbmTestSuite {

}
