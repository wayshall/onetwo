package org.onetwo.common.jfish;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.example.app.model.member.UserVersionServiceImpl;
import org.example.app.model.member.entity.UserVersionEntity;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.fish.exception.JFishEntityVersionException;
import org.onetwo.common.utils.DateUtil;

public class JFishVersionableTest extends JFishBaseJUnitTest {
	
	@Resource
	private UserVersionServiceImpl userServiceImpl;
	static ExecutorService executor = Executors.newSingleThreadExecutor();
	private static Long userId;
	final CountDownLatch countDown = new CountDownLatch(1);
	


	protected UserVersionEntity createUserVersionEntity(int i, String userName){
		UserVersionEntity user = new UserVersionEntity();
		user.setUserName(userName+i);
		user.setBirthDay(DateUtil.now());
		user.setEmail(i+"username@qq.com");
		user.setHeight(3.3f);
		user.setAge(11);
		
		return user;
	}
	
	@AfterClass
	public static void clear(){
		executor.shutdownNow();
	}

	@Test
	public void testCreateUser() throws Exception{
		Assert.assertNotNull(userServiceImpl);
		final UserVersionEntity user = this.createUserVersionEntity(1, "test1");
		userServiceImpl.save(user);
		userId = user.getId();
		Assert.assertTrue(1L==user.getVersion());
		

		Assert.assertNotNull(userId);
		UserVersionEntity dbuser = userServiceImpl.findById(userId);
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
		UserVersionEntity dbuser = userServiceImpl.findById(userId);
		Assert.assertTrue(3L==dbuser.getVersion());
		
		//利用线程调用另一个test方法，避免同一个test方法同一个事务的问题
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
//			userServiceImpl.save(dbuser);这个接口标注了事务，会回滚
			userServiceImpl.getBaseEntityManager().save(dbuser);
			Assert.fail("should failed in here!");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(JFishEntityVersionException.class.isInstance(e));
			Assert.assertTrue(3L==dbuser.getVersion());

			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					testAssertQueryInOtherThread();
				}
			});
			
		}
	}
	
	public void testSaveInThread() {
		System.out.println("-------------for thread");
		try {
			UserVersionEntity dbuser = userServiceImpl.findById(userId);
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
	
	public void testAssertQueryInOtherThread() {
		//已修改为4
		UserVersionEntity dbuser = userServiceImpl.findById(userId);
		Assert.assertTrue(4L==dbuser.getVersion());
	}

}
