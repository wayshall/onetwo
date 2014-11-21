package org.onetwo.app;

import javax.annotation.Resource;

import org.junit.Test;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.test.dbtest.model.service.UserAutoidServiceImpl;

//@TransactionConfiguration(defaultRollback=true)
public class BatchInsertTest extends AppBaseTest {
	
	@Resource
	private UserAutoidServiceImpl userAutoidServiceImpl;
	
	@Test
	public void testBatchInsert(){
		TimeCounter t = new TimeCounter("testBatchInsert");
		t.start();
		this.userAutoidServiceImpl.saveList(1000);
		t.stop();
	}

}
