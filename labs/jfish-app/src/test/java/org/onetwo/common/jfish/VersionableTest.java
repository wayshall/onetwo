package org.onetwo.common.jfish;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.example.app.model.member.UserServiceImpl;
import org.example.app.model.member.entity.UserEntity;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.fish.exception.JFishEntityVersionException;
import org.onetwo.common.utils.LangUtils;

public class VersionableTest extends JFishBaseJUnitTest {
	
	@Resource
	private UserServiceImpl userServiceImpl;
	static ExecutorService executor = Executors.newSingleThreadExecutor();
	private static Long userId;
	final CountDownLatch countDown = new CountDownLatch(1);
	
	@AfterClass
	public static void clear(){
		executor.shutdownNow();
	}

	@Test
	public void testCreateUser() throws Exception{
		Assert.assertNotNull(userServiceImpl);
		final UserEntity user = this.createUserEntity(1, "test1");
		userServiceImpl.save(user);
		userId = user.getId();
		Assert.assertTrue(1L==user.getVersion());
		

		Assert.assertNotNull(userId);
		UserEntity dbuser = userServiceImpl.findById(userId);
		Assert.assertNotNull(dbuser.getId());
		Assert.assertTrue(1L==dbuser.getVersion());
		
		dbuser.setUserName("test-forupdate");
		userServiceImpl.save(dbuser);
		Assert.assertNotNull(dbuser.getId());
		Assert.assertEquals("test-forupdate", dbuser.getUserName());
		Assert.assertTrue(2L==dbuser.getVersion());

		dbuser.setUserName("test-forupdate3");
		userServiceImpl.getBaseEntityManager().getJfishDao().update(dbuser);
		Assert.assertEquals("test-forupdate3", dbuser.getUserName());
		Assert.assertTrue(3L==dbuser.getVersion());
	}

	@Test
	public void testConcurrentSave() throws Exception{
		UserEntity dbuser = userServiceImpl.findById(userId);
		Assert.assertTrue(3L==dbuser.getVersion());a
		
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				testSaveInThread();
			}
		});

		countDown.await();
		System.out.println("-------------for concurrent");
		dbuser.setUserName("test-forupdate-concurrent");
		try {
			userServiceImpl.save(dbuser);
			Assert.fail("should failed in here!");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(JFishEntityVersionException.class.isInstance(e));
		}
	}
	
	public void testSaveInThread() {
		System.out.println("-------------for thread");
		try {
			UserEntity dbuser = userServiceImpl.findById(userId);
			Assert.assertNotNull(dbuser);
			Assert.assertTrue(3L==dbuser.getVersion());
			
			dbuser.setUserName("thread-forupdate4");
			userServiceImpl.save(dbuser);
			Assert.assertEquals("thread-forupdate4", dbuser.getUserName());
			Assert.assertTrue(4L==dbuser.getVersion());
		} finally{
			System.out.println("-------------countdown");
			countDown.countDown();
		}
	}

}
