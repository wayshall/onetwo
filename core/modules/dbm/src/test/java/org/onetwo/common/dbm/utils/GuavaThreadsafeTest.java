package org.onetwo.common.dbm.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaThreadsafeTest {
	
	@Test
	public void testLocalCache() throws Exception{
		AtomicInteger count = new AtomicInteger(0);
		final Cache<String, String> caches = CacheBuilder.newBuilder().build();
		
		Thread t1 = new Thread(()->{
			try {
				System.out.println("run thread:" + Thread.currentThread().getName());
				String res = caches.get("test", ()->{
					count.incrementAndGet();
					System.out.println("cache thread:" + Thread.currentThread().getName());
					Thread.sleep(1000);
					return "testvalue";
				});
				System.out.println(res+" thread:" + Thread.currentThread().getName());

				Assert.assertEquals("testvalue", res);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t1.start();
		

		Thread t2 = new Thread(()->{
			try {
				System.out.println("run thread:" + Thread.currentThread().getName());
//				Thread.sleep(500);
				String res = caches.get("test", ()->{
					count.incrementAndGet();
					System.out.println("cache thread:" + Thread.currentThread().getName());
					return "testvalue2";
				});
				System.out.println(res+" thread:" + Thread.currentThread().getName());
				
				Assert.assertEquals("testvalue", res);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t2.start();
		
		t1.join();
		t2.join();

		Assert.assertEquals(1, count.get());
	}
	

}
