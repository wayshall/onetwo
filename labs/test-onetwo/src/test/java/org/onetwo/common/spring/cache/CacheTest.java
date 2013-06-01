package org.onetwo.common.spring.cache;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.spring.UserService;
import org.onetwo.common.test.spring.SpringTxJUnitTestCase;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import test.entity.UserEntity;

@ContextConfiguration(locations = { "/org/onetwo/common/spring/cache/applicationContext-test.xml" })
public class CacheTest extends SpringTxJUnitTestCase {
	
	@Autowired
	private UserService userService;

	@BeforeClass
	public static void beforeClass(){
		UtilTimerStack.setActive(true);
	}
	
	@Test
	public void testCacheByKey(){
		UserEntity user = this.userService.findByName("zeng");
		Assert.assertNotNull(user);
		
		UserEntity user2 = this.userService.findByName("zeng");
		Assert.assertTrue((user==user2));
		
		UserEntity user3 = this.userService.findByName("way");
		Assert.assertFalse((user==user3));
	}
	
	@Test
	public void testCache(){
		UserEntity user = this.userService.findUser(1l);
		Assert.assertNotNull(user);
		
		UserEntity user2 = this.userService.findUser(1l);
		Assert.assertTrue((user==user2));
		
		UserEntity user3 = this.userService.findUser(3l);
		Assert.assertFalse((user==user3));
	}
	
	@Test
	public void testFlushCache(){
		Long id = 1l;
		UserEntity user = this.userService.findUser(id);
		Assert.assertNotNull(user);
		
		UserEntity user2 = this.userService.findUser(id);
		Assert.assertTrue((user==user2));
		
		UserEntity saveUser = new UserEntity();
		saveUser.setId(id);
		this.userService.saveUser(saveUser);
		UserEntity newUser = this.userService.findUser(id);
		Assert.assertTrue((newUser!=user2));
		
		user2 = this.userService.findUser(id);
		Assert.assertTrue((newUser==user2));
	}
	
	@Test
	public void testCacheWithExpire(){
		Page<UserEntity> users = this.userService.findUserByPage();
		Assert.assertNotNull(users);
//		System.out.println("page user:" + users);
		
		Page<UserEntity> users2 = this.userService.findUserByPage();
//		System.out.println("page user2 : " + users2);
		Assert.assertTrue((users==users2));
		
		LangUtils.await(2);
		users2 = this.userService.findUserByPage();
//		System.out.println("page user22 : " + users2);
		Assert.assertTrue((users==users2));
		

		LangUtils.await(2);
		
		Page<UserEntity> users3 = this.userService.findUserByPage();
//		System.out.println("page user3 : " + users3);
		Assert.assertTrue((users!=users3));
		Assert.assertTrue((users2!=users3));
	}

}
