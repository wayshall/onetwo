package org.onetwo.common.dbm;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity;
import org.onetwo.common.dbm.model.entity.UserAutoidEntity.UserStatus;
import org.onetwo.common.dbm.model.service.UserAutoidServiceImpl;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.support.DbmEntityManager;
import org.springframework.transaction.annotation.Transactional;

//@TransactionConfiguration(defaultRollback=true)
@Transactional
public class DbmDaoTest extends DbmBaseTest {
	
	@Resource
	private DbmEntityManager jfishEntityManager;
	@Resource
	private UserAutoidServiceImpl userAutoidServiceImpl;
	
	private UserAutoidEntity createUserAutoidEntity(String userNamePrefix,  int i){
		UserAutoidEntity user = new UserAutoidEntity();
		user.setUserName(userNamePrefix+"-batch-"+i);
		user.setPassword("password-batch-"+i);
//		user.setCreateTime(new Date());
		user.setGender(i%2);
		user.setNickName("nickName-batch-"+i);
		user.setEmail("test@qq.com");
		user.setMobile("137"+i);
		user.setBirthday(new Date());
		user.setStatus(UserStatus.NORMAL);
		return user;
	}
	
	@Test
	public void testInsert(){
		int insertCount = 1000;
		//精确到秒，否则会有误差，比如2015-05-06 13:49:09.783存储到mysql后会变成2015-05-06 13:49:10，mysql的datetime只能精确到秒
		TimeCounter t = new TimeCounter("testInsert");
		t.start();
		String userName = "unique_user_name______________________";
		Stream.iterate(1, item->item+1).limit(insertCount).forEach(item->{
			UserAutoidEntity entity = createUserAutoidEntity("test", item);
			entity.setUserName(userName);
			jfishEntityManager.save(entity);
		});
		t.stop();
		
		UserAutoidEntity user = jfishEntityManager.findOne(UserAutoidEntity.class, "userName", userName);
		Assert.assertEquals(UserStatus.NORMAL, user.getStatus());
		
		userAutoidServiceImpl.deleteAll();
	}
	
	@Test
	public void testInsertList(){
		int insertCount = 1000;
		//精确到秒，否则会有误差，比如2015-05-06 13:49:09.783存储到mysql后会变成2015-05-06 13:49:10，mysql的datetime只能精确到秒
		TimeCounter t = new TimeCounter("testInsertList");
		t.start();
		List<UserAutoidEntity> userlist = Stream.iterate(1, item->item+1).limit(insertCount)
					.map(i->createUserAutoidEntity("testList", i))
					.collect(Collectors.toList());
		
		jfishEntityManager.save(userlist);	
		t.stop();
		
		userAutoidServiceImpl.deleteAll();
	}
	
//	@Test
	//deadlock..........
	public void testMultipThread() throws Exception{
		int taskCount = 2;
		CyclicBarrier barrier = new CyclicBarrier(taskCount);
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(taskCount);
		Future<?> res1 = executor.submit(()->{
			try {
				System.out.println("await.....");
				barrier.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
			testInsert();
		});
		LangUtils.await(2);
		Future<?> res2 = executor.submit(()->{
			try {
				System.out.println("await.....");
				barrier.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
			testInsert();
		});

		res1.get();
		res2.get();
		System.out.println("done");
		
	}

}
