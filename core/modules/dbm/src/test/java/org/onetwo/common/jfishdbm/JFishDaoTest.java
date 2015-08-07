package org.onetwo.common.jfishdbm;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.Test;
import org.onetwo.common.jfishdbm.model.entity.UserAutoidEntity;
import org.onetwo.common.jfishdbm.model.service.UserAutoidServiceImpl;
import org.onetwo.common.jfishdbm.support.JFishEntityManager;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.LangUtils;

//@TransactionConfiguration(defaultRollback=true)
public class JFishDaoTest extends AppBaseTest {
	
	@Resource
	private JFishEntityManager jfishEntityManager;
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
		return user;
	}
	
	@Test
	public void testInsert(){
		int insertCount = 1000;
		//精确到秒，否则会有误差，比如2015-05-06 13:49:09.783存储到mysql后会变成2015-05-06 13:49:10，mysql的datetime只能精确到秒
		TimeCounter t = new TimeCounter("testInsert");
		t.start();
		Stream.iterate(1, item->item+1).limit(insertCount).forEach(item->{
			UserAutoidEntity entity = createUserAutoidEntity("test", item);
			jfishEntityManager.save(entity);
		});
		t.stop();
		
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
