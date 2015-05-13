package org.onetwo.common.jfish;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.jfishdb.spring.JFishDaoImpl;
import org.onetwo.common.nutz.NutzBaseDao;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.test.spring.SpringTxJUnitTestCase;
import org.onetwo.common.utils.DateUtil;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

import test.entity.UserEntity;

@ActiveProfiles({"test"})
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@TransactionConfiguration(defaultRollback=false)
public class SavePerformanceTest extends SpringTxJUnitTestCase {
	
	@Resource
	private JFishDaoImpl jdao;

	@Resource
	private DataSource dataSource;
	private NutzBaseDao nutzDao;
	
	private int insertCount = 100;
	

	@Before
	public void setup(){
		nutzDao = new NutzBaseDao(dataSource);
		UtilTimerStack.setActive(true);
		String pname = "test";
		UtilTimerStack.push(pname);
		UtilTimerStack.pop(pname);

		UserEntity setup = createUserEntity(0, "setup");
		this.nutzDao.insert(setup);
		setup = createUserEntity(1, "setup");
		this.jdao.insert(setup);
	}
	

	@Test
	public void testJFishSave(){
		String name = "testJFishSave";
		UtilTimerStack.push(name);
		for(int i=0; i<insertCount; i++){
			UserEntity user = createUserEntity(i, "testJFishSave");
			jdao.insert(user);
//			System.out.println("id:"+user.getId());
//			Assert.assertNotNull(user.getId());
		}
		UtilTimerStack.pop(name);
	}
	
	protected UserEntity createUserEntity(int i, String userName){
		UserEntity user = new UserEntity();
		user.setUserName(userName+i);
		user.setBirthDay(DateUtil.now());
		user.setEmail(i+"username@qq.com");
		user.setHeight(3.3f);
		
		return user;
	}
	
	@Test
	public void testJFishSaveList(){
		UtilTimerStack.setActive(true);
		String name = "testJFishSaveList";
		UtilTimerStack.push(name);
		List<UserEntity> list = new ArrayList<UserEntity>();
		for(int i=0; i<insertCount; i++){
			UserEntity user = createUserEntity(i, "testJFishSaveList");
			list.add(user);
//			System.out.println("id:"+user.getId());
//			Assert.assertNotNull(user.getId());
		}
		jdao.insert(list);
		UtilTimerStack.pop(name);
	}
	
	@Test
	public void testJFishBatchSave(){
		UtilTimerStack.setActive(true);
		String name = "testJFishBatchSave";
		UtilTimerStack.push(name);
		List<UserEntity> list = new ArrayList<UserEntity>();
		for(int i=0; i<insertCount; i++){
			UserEntity user = createUserEntity(i, "testJFishBatchSave");
			list.add(user);
//			System.out.println("id:"+user.getId());
//			Assert.assertNotNull(user.getId());
		}
		int total = jdao.batchInsert(list);
		UtilTimerStack.pop(name);
		Assert.assertTrue(total==insertCount);
	}

	@Test
	public void testNutzSave(){
		UtilTimerStack.setActive(true);
		String name = "testNutzSave";
		UtilTimerStack.push(name);
		for(int i=0; i<insertCount; i++){
			UserEntity user = createUserEntity(i, "testNutzSave");
			nutzDao.insert(user);
			
		}
		UtilTimerStack.pop(name);
	}

	@Test
	public void testAll(){
		this.testJFishBatchSave();
		this.testJFishSaveList();
		this.testNutzSave();
		this.testJFishSave();
		System.out.println("---------------------------------------------------------------------");

		/*this.testJFishBatchSave();
		this.testJFishSaveList();
		this.testNutzSave();
		this.testJFishSave();
		System.out.println("---------------------------------------------------------------------");

		this.testJFishBatchSave();
		this.testJFishSaveList();
		this.testNutzSave();
		this.testJFishSave();
		System.out.println("---------------------------------------------------------------------");
		*/
	}
}
