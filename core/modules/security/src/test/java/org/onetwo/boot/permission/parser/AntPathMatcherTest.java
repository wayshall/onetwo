package org.onetwo.boot.permission.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;

public class AntPathMatcherTest {
	
	
	@Test
	public void testAntMatcher(){
		AntPathMatcher req = new AntPathMatcher();
		boolean res = req.match("/user.*", "/user.json");
		Assert.assertTrue(res);
		
		res = req.match("/user*", "/user");
		Assert.assertTrue(res);
		res = req.match("/user*", "/user.json");
		Assert.assertTrue(res);
		res = req.match("/user*", "/userInfo");
		Assert.assertTrue(res);
		res = req.match("/user*", "/user/1");
		Assert.assertFalse(res);

		res = req.match("/user**", "/user");
		Assert.assertTrue(res);
		res = req.match("/user**", "/user.json");
		Assert.assertTrue(res);
		res = req.match("/user**", "/userInfo");
		Assert.assertTrue(res);
		res = req.match("/user**", "/user/1.json");

		res = req.match("/user/*", "/user/1");
		Assert.assertTrue(res);
		res = req.match("/user/*", "/user/1.json");
		Assert.assertTrue(res);
		res = req.match("/user/*", "/user/aaa/1.json");
		Assert.assertFalse(res);
		

		Assert.assertFalse(res);
		res = req.match("/user/**", "/user/1.json");
		Assert.assertTrue(res);
		res = req.match("/user/**", "/user/aaa/1.json");
		Assert.assertTrue(res);
	}
	
	@Test
	public void testOrder(){
		List<String> urls = new ArrayList<String>();
		urls.add("/userInfo*");
		urls.add("/user*");
		urls.add("/userRole*");
		
		System.out.println("list:"+urls);
		Collections.sort(urls);
		Collections.reverse(urls);
		System.out.println("list:"+urls);
	}

}
