package org.onetwo.plugins.jsonrpc.client.test;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.jsonrpc.protocol.NamedParamsRequest;
import org.onetwo.plugins.jsonrpc.client.proxy.ToJsonStringHandler;

public class ToJsonStringTest {
	
	@Test
	public void testOneParameter(){
		ToJsonStringHandler handler = new ToJsonStringHandler(RpcUserServiceTest.class);
		RpcUserServiceTest userService = handler.getProxyObject();
		NamedParamsRequest req = userService.findById(1L);
		Assert.assertTrue(req.getParams().containsKey("id"));
		Assert.assertEquals(1L, req.getParams().get("id"));
		
		String jsonstr = JsonMapper.defaultMapper().toJson(req);
		System.out.println("jsonstr: " + jsonstr);
		String expected = "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById\",\"params\":{\"id\":1}}";
		Assert.assertEquals(expected, jsonstr);
	}
	
	@Test
	public void testTwoParameter(){
		ToJsonStringHandler handler = new ToJsonStringHandler(RpcUserServiceTest.class);
		RpcUserServiceTest userService = handler.getProxyObject();
		NamedParamsRequest req = userService.findByUserNameAndAge("way", 30);
		Assert.assertTrue(req.getParams().containsKey("userName"));
		Assert.assertTrue(req.getParams().containsKey("age"));
		Assert.assertEquals("way", req.getParams().get("userName"));
		Assert.assertEquals(30, req.getParams().get("age"));
		
		String jsonstr = JsonMapper.defaultMapper().toJson(req);
		System.out.println("jsonstr: " + jsonstr);
		String expected = "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findByUserNameAndAge\",\"params\":{\"userName\":\"way\",\"age\":30}}";
		Assert.assertEquals(expected, jsonstr);
	}

}
