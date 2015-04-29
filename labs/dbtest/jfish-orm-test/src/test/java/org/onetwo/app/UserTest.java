package org.onetwo.app;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.test.dbtest.model.entity.UserAutoidEntity;


public class UserTest extends AppBaseTest {
	
	@Resource
	private BaseEntityManager baseEntityManager;
	
	@Test
	public void testSaveUser(){
		int i = 1;
		UserAutoidEntity user = new UserAutoidEntity();
		user.setUserName("test1");
		user.setPassword("password-batch-"+i);
		user.setGender(i%2);
		user.setNickName("nickName-batch-"+i);
		user.setEmail("test@qq.com");
		user.setMobile("137"+i);
		
		baseEntityManager.save(user);
	}

}
