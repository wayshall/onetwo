package org.onetwo.common.jfishdbm;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.jfishdbm.model.entity.UserAutoidEntity.UserStatus;
import org.onetwo.common.jfishdbm.model.service.UserAutoidServiceImpl;
import org.onetwo.common.profiling.TimeCounter;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.NiceDate;

//@TransactionConfiguration(defaultRollback=true)
public class OneBatchInsertTest extends AppBaseTest {
	
	@Resource
	private UserAutoidServiceImpl userAutoidServiceImpl;
	
//	@Test
	public void testBatchInsert(){
		int insertCount = 10000;
		//精确到秒，否则会有误差，比如2015-05-06 13:49:09.783存储到mysql后会变成2015-05-06 13:49:10，mysql的datetime只能精确到秒
		NiceDate niceNowSeconde = NiceDate.New().thisSec();
		TimeCounter t = new TimeCounter("OneBatchInsertTest");
		t.start();
		int count = this.userAutoidServiceImpl.daoBatchInsert("testBatchInsert", UserStatus.NORMAL, niceNowSeconde.getTime(), insertCount);
		Assert.assertEquals(insertCount, count);
		t.stop();
		
		userAutoidServiceImpl.deleteAll();
	}
	
	@Test
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
			testBatchInsert();
		});
		LangUtils.await(2);
		Future<?> res2 = executor.submit(()->{
			try {
				System.out.println("await.....");
				barrier.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
			testBatchInsert();
		});

		res1.get();
		res2.get();
		System.out.println("done");
		
	}

}
