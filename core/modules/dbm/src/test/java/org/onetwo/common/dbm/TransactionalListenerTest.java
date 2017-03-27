package org.onetwo.common.dbm;

import org.junit.Test;
import org.onetwo.common.base.DbmBaseTest;
import org.onetwo.common.dbm.model.service.TransationalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionalListenerTest extends DbmBaseTest {
	
	@Autowired
	private TransationalServiceImpl transationalServiceImpl;
	@Test
	public void testListener(){
		transationalServiceImpl.save();
	}

}
