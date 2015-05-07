package org.onetwo.common.jfishdbm.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.onetwo.common.jfishdbm.BatchInsertTest;
import org.onetwo.common.jfishdbm.DBCheckerTest;
import org.onetwo.common.jfishdbm.model.service.UserAutoidServiceTest;

@RunWith(Suite.class)
@SuiteClasses({DBCheckerTest.class, UserAutoidServiceTest.class, BatchInsertTest.class})
public class DbmTestSuite {

}
