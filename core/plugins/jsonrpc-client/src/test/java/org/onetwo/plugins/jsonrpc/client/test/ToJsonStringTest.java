package org.onetwo.plugins.jsonrpc.client.test;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.jsonrpc.protocol.JsonRpcParamsRequest;
import org.onetwo.common.jsonrpc.protocol.ListParamsRequest;
import org.onetwo.common.jsonrpc.protocol.NamedParamsRequest;


public class ToJsonStringTest {
	
	@Test
	public void testOneParameter(){
		ToParamsJsonStringHandler handler = new ToParamsJsonStringHandler(RpcUserServiceTest.class);
		RpcUserServiceTest userService = handler.getProxyObject();
		NamedParamsRequest req = userService.findById(1L);
		Assert.assertTrue(req.getParams().containsKey("id"));
		Assert.assertEquals(1L, req.getParams().get("id"));
		
		String jsonstr = JsonMapper.defaultMapper().toJson(req);
		System.out.println("jsonstr: " + jsonstr);
		String expected = "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById\",\"params\":{\"id\":1}}";
		Assert.assertEquals(expected, jsonstr);
		
		NamedParamsRequest fromJsonReq = JsonMapper.defaultMapper().fromJson(jsonstr, NamedParamsRequest.class);
		Assert.assertEquals("2.0", fromJsonReq.getJsonrpc());
		Assert.assertEquals("org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById", fromJsonReq.getMethod());
		Assert.assertTrue(req.getParams().containsKey("id"));
		Assert.assertEquals(1L, req.getParams().get("id"));
	}
	
	@Test
	public void testOneParameter4List(){
		ToListJsonStringHandler handler = new ToListJsonStringHandler(RpcUserServiceTest.class);
		RpcUserServiceTest userService = handler.getProxyObject();
		ListParamsRequest req = userService.findById4List(1L);
		Assert.assertTrue(req.getParams().size()==1);
		Assert.assertEquals(1L, req.getParams().get(0));
		
		String jsonstr = JsonMapper.defaultMapper().toJson(req);
		System.out.println("jsonstr: " + jsonstr);
		String expected = "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById4List\",\"params\":[1]}";
		Assert.assertEquals(expected, jsonstr);
		
		ListParamsRequest fromJsonReq = JsonMapper.defaultMapper().fromJson(jsonstr, ListParamsRequest.class);
		Assert.assertEquals("2.0", fromJsonReq.getJsonrpc());
		Assert.assertEquals("org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById4List", fromJsonReq.getMethod());
		Assert.assertTrue(req.getParams().size()==1);
		Assert.assertEquals(1L, req.getParams().get(0));
	}
	
	@Test
	public void testOneParameter4Object(){
		String jsonstr = "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById4List\",\"params\":[1]}";
		
		JsonRpcParamsRequest fromJsonReq = JsonMapper.defaultMapper().fromJson(jsonstr, JsonRpcParamsRequest.class);
		Assert.assertEquals("2.0", fromJsonReq.getJsonrpc());
		Assert.assertEquals("org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById4List", fromJsonReq.getMethod());
		Assert.assertTrue(fromJsonReq.getParams(List.class).size()==1);
		Assert.assertEquals(1, fromJsonReq.getParams(List.class).get(0));
		

		ToObjectJsonStringHandler handler = new ToObjectJsonStringHandler(true, RpcUserServiceTest.class);
		RpcUserServiceTest userService = handler.getProxyObject();
		RpcUserVo user = new RpcUserVo();
		user.setUserName("way2");
		user.setAge(30);
		user.setBirthday(new Date());
		JsonRpcParamsRequest req = userService.save4Object("way", user);
		jsonstr = JsonMapper.DEFAULT_MAPPER.toJson(req);
		System.out.println("testOneParameter4Object: " + jsonstr);
		
		req = JsonMapper.DEFAULT_MAPPER.fromJson(jsonstr, JsonRpcParamsRequest.class);
		System.out.println("req: " +req);
	}
	
	@Test
	public void testTwoParameter(){
		ToParamsJsonStringHandler handler = new ToParamsJsonStringHandler(RpcUserServiceTest.class);
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
	
	@Test
	public void testTwoParameter4List(){
		ToListJsonStringHandler handler = new ToListJsonStringHandler(RpcUserServiceTest.class);
		RpcUserServiceTest userService = handler.getProxyObject();
		ListParamsRequest req = userService.findByUserNameAndAge4List("way", 30);
		Assert.assertTrue(req.getParams().size()==2);
		Assert.assertEquals("way", req.getParams().get(0));
		Assert.assertEquals(30, req.getParams().get(1));
		
		String jsonstr = JsonMapper.defaultMapper().toJson(req);
		System.out.println("jsonstr: " + jsonstr);
		String expected = "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findByUserNameAndAge4List\",\"params\":[\"way\",30]}";
		Assert.assertEquals(expected, jsonstr);
	}

}
