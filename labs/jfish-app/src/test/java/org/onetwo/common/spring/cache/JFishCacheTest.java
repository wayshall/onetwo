package org.onetwo.common.spring.cache;

import java.util.Collection;

import javax.annotation.Resource;

import org.example.app.model.member.entity.UserEntity;
import org.example.app.model.member.service.impl.TestCacheServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.jfish.JFishBaseJUnitTest;
import org.onetwo.common.utils.Page;

public class JFishCacheTest extends JFishBaseJUnitTest {
	
	@Resource
	private JFishSimpleCacheManagerImpl cacheManager;
	
	@Resource
	private TestCacheServiceImpl testCacheServiceImpl;
	
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
	
	@Test
	public void testDemoCacheByPage(){
		String keyword = "page-test";
		Page<UserEntity> page = Page.create();
		page = this.testCacheServiceImpl.findPageByName(page, keyword);

		Page<UserEntity> page2 = Page.create();
		page2 = this.testCacheServiceImpl.findPageByName(page2, keyword);
		Assert.assertSame(page, page2);

	}
	

}
