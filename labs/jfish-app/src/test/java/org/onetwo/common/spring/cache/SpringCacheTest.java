package org.onetwo.common.spring.cache;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Statistics;

import org.example.app.model.member.entity.UserEntity;
import org.example.app.model.member.service.impl.TestCacheServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.jfish.JFishBaseJUnitTest;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.springframework.cache.ehcache.EhCacheCacheManager;

public class SpringCacheTest extends JFishBaseJUnitTest {
	
	@Resource
	private EhCacheCacheManager cacheManager;
	
	@Resource
	private TestCacheServiceImpl testCacheServiceImpl;
	
	private Cache demoCache;
	
	@Before
	public void setup(){
	}
	@Test
	public void testCacheManager(){
		Collection<String> cacheNames = this.cacheManager.getCacheNames();
		for(String name : cacheNames){
			System.out.println("name: " + name);
		}
	}
	
	private void printlnStatus(String tag, String name){
		LangUtils.println("=============>> " + tag);
		Cache demoCache = this.cacheManager.getCacheManager().getCache(name);
		List list = demoCache.getKeys();
		for(Object obj : list){
			System.out.println("obj:"+obj);
		}
		Statistics statiscs = demoCache.getStatistics();
		System.out.println("Statistics: " + statiscs); 
	}

	private void printlnStatus(String tag){
		printlnStatus(tag, "demoCache");
	}
	
	@Test
	public void testDemoCacheByPage(){
		String keyword = "page-test";
		Page<UserEntity> page = Page.create();
		page = this.testCacheServiceImpl.findPageByName(page, keyword);

		printlnStatus("=============>> testDemoCacheByPage : findPageByName1 invoked");
		
		Page<UserEntity> page2 = Page.create();
		page2 = this.testCacheServiceImpl.findPageByName(page2, keyword);
		Assert.assertSame(page, page2);
		printlnStatus("=============>> testDemoCacheByPage : findPageByName2 invoked");
		
		LangUtils.await(6);
		printlnStatus("=============>> testDemoCacheByPage : await(6) to expired");
		
		Page<UserEntity> page3 = Page.create();
		page3 = this.testCacheServiceImpl.findPageByName(page3, keyword);
		Assert.assertNotSame(page, page3);

		printlnStatus("=============>> testDemoCacheByPage : findPageByName3 invoked");
	}
	
	@Test
	public void testDemoCacheByList(){
		final String keyword = "list-test";
		List<UserEntity> list = null;
		list = this.testCacheServiceImpl.findListByName(keyword);
		printlnStatus("=============>> testDemoCacheByList : findListByName invoked");
		
		List<UserEntity> list2 = this.testCacheServiceImpl.findListByName(keyword);
		Assert.assertSame(list, list2);
		printlnStatus("=============>> testDemoCacheByList : findListByName2 invoked");
		
		new Thread(){

			@Override
			public void run() {
				while(true){
					List<UserEntity> list4 = testCacheServiceImpl.findListByName(keyword);
					printlnStatus("=============>> testDemoCacheByList : findListByName2 invoked " + list4.hashCode());
					LangUtils.await(1);
				}
			}
			
		}.start();
		
		LangUtils.await(10);
		printlnStatus("=============>> testDemoCacheByList : await(6) to expired");
		
		List<UserEntity> list3 = this.testCacheServiceImpl.findListByName(keyword);
		Assert.assertNotSame(list, list3);
		LangUtils.println("=============>> testDemoCacheByList : findListByName3 invoked");

		LangUtils.CONSOLE.exitIf("exit");
	}

}
