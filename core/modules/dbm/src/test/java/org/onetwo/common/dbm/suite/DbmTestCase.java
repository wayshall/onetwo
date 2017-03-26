package org.onetwo.common.dbm.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.onetwo.common.dbm.BaseCrudEntityManagerTest;
import org.onetwo.common.dbm.BatchInsertTest;
import org.onetwo.common.dbm.DbmDaoTest;
import org.onetwo.common.dbm.DbmEntityManagerTest;
import org.onetwo.common.dbm.DbmNestedMappingTest;
import org.onetwo.common.dbm.TransactionalListenerTest;

@RunWith(Suite.class)
@SuiteClasses({
	DbmDaoTest.class,
	DbmEntityManagerTest.class,
//	OneBatchInsertTest.class,
	BatchInsertTest.class,
	BaseCrudEntityManagerTest.class,
	DbmNestedMappingTest.class,
	TransactionalListenerTest.class,
//	RichModelTest.class
})
public class DbmTestCase {

}
