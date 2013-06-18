package org.onetwo.common.jfish;

import javax.annotation.Resource;

import org.example.app.model.member.UserServiceImpl;
import org.example.app.model.member.entity.UserEntity;
import org.junit.Assert;
import org.junit.Test;

public class VersionableTest extends JFishBaseJUnitTest {
	
	@Resource
	private UserServiceImpl userServiceImpl;
	
	@Test
	public void testUserService(){
		Assert.assertNotNull(userServiceImpl);
		UserEntity user = this.createUserEntity(1, "test1");
		userServiceImpl.save(user);
		
		UserEntity dbuser = userServiceImpl.findById(user.getId());
		Assert.assertNotNull(dbuser.getId());
		Assert.assertEquals(1L, dbuser.getVersion());
		
		dbuser.setUserName("test-forupdate");
		userServiceImpl.save(user);
		Assert.assertNotNull(dbuser.getId());
		Assert.assertEquals("test-forupdate", dbuser.getUserName());
		Assert.assertEquals(2L, dbuser.getVersion());
		
	}

}
