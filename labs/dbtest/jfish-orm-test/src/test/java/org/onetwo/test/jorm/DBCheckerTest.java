package org.onetwo.test.jorm;

import javax.annotation.Resource;

import org.junit.Test;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.test.jorm.model.entity.UserAutoidEntity;
import org.onetwo.test.jorm.model.entity.UserEntity;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Assert;

@TransactionConfiguration(defaultRollback=true)
public class DBCheckerTest extends AppBaseTest {
	
	@Resource
	private BaseEntityManager baseEntityManager;

	@Test
	public void testSaveUser(){
		UserEntity user = new UserEntity();
		String userName = "test_user_name";
		user.setUserName(userName);
		user.setNickName("test_nickName");
		baseEntityManager.save(user);
		UserEntity dbuser = baseEntityManager.findOne(UserEntity.class, "userName", userName);
		Assert.notNull(dbuser);
		Assert.notNull(dbuser.getId());
	}
	
	@Test
	public void testSaveUserAutoid(){
		UserAutoidEntity user = new UserAutoidEntity();
		String userName = "test_user_name";
		user.setUserName(userName);
		user.setNickName("test_nickName");
		baseEntityManager.save(user);
		UserAutoidEntity dbuser = baseEntityManager.findOne(UserAutoidEntity.class, "userName", userName);
		Assert.notNull(dbuser);
		Assert.notNull(dbuser.getId());
	}

}
