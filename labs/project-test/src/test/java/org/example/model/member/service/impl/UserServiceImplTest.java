package org.example.model.member.service.impl;

import java.util.List;

import org.example.AppBaseTest;
import org.example.model.member.entity.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImplTest extends AppBaseTest {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Test
	public void testFindByUserName(){
		List<UserEntity> users = this.userServiceImpl.findByProperties("userName:like", "test");
		Assert.assertNull(users);
	}
}
