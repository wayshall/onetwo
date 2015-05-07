package org.onetwo.test.jorm;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.jfishdbm.support.DaoFactory;
import org.onetwo.common.jfishdbm.support.JFishDao;
import org.onetwo.test.jorm.model.entity.UserAutoidEntity;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Assert;

@TransactionConfiguration(defaultRollback=true)
public class DBCheckerTest extends AppBaseTest {
	
	@Resource
	private DataSource dataSource;
	
	private JFishDao jfishDao;
	
	@Before
	public void setup(){
		this.jfishDao = DaoFactory.getInstance().newDao(dataSource);
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
