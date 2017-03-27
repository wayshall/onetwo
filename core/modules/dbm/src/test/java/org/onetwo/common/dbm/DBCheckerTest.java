package org.onetwo.common.dbm;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.base.DbmBaseTest;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity;
import org.onetwo.dbm.core.spi.DbmSession;
import org.onetwo.dbm.utils.Dbms;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Assert;

@TransactionConfiguration(defaultRollback=true)
public class DBCheckerTest extends DbmBaseTest {
	
	@Resource
	private DataSource dataSource;
	
	private DbmSession jfishDao;
	
	@Before
	public void setup(){
		this.jfishDao = Dbms.newSessionFactory(dataSource).openSession();
	}
	
	@Test
	public void testSaveUserAutoid(){
		UserAutoidEntity user = new UserAutoidEntity();
		String userName = "test_user_name";
		user.setUserName(userName);
		user.setNickName("test_nickName");
		this.jfishDao.save(user);
		Assert.notNull(user);
		Assert.notNull(user.getId());
	}

}
