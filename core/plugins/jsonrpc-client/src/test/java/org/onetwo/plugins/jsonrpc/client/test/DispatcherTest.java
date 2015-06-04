package org.onetwo.plugins.jsonrpc.client.test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.jsonrpc.JsonRpcParser;
import org.onetwo.common.jsonrpc.protocol.DispatchableMethod;
import org.onetwo.common.jsonrpc.protocol.JsonRpcParamsRequest;
import org.onetwo.common.utils.ReflectUtils;

public class DispatcherTest {
	
	@Test
	public void test() throws Exception{
//		String jsonstr = "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById\",\"params\":{\"id\":1}}";
//		JsonRpcParamsRequest req = JsonMapper.DEFAULT_MAPPER.fromJson(jsonstr, JsonRpcParamsRequest.class);
		
		ToObjectJsonStringHandler handler = new ToObjectJsonStringHandler(true, RpcUserServiceTest.class);
		RpcUserServiceTest userService = handler.getProxyObject();
		RpcUserVo user = new RpcUserVo();
		user.setUserName("way2");
		user.setAge(30);
		user.setBirthday(new Date());
		JsonRpcParamsRequest req = userService.save4Object("way", user);
		String jsonstr = JsonMapper.DEFAULT_MAPPER.toJson(req);
		System.out.println("testOneParameter4Object: " + jsonstr);

		JsonRpcParser jp = new JsonRpcParser(jsonstr);
		JsonRpcParamsRequest parsedReq = jp.parseHeader();
		DispatchableMethod dreq = new DispatchableMethod(parsedReq.getMethod());
		System.out.println("class: " + dreq.getServiceClassName());
		System.out.println("method: " + dreq.getServiceMethodName());
		
		Class<?> serviceClass = ReflectUtils.loadClass(dreq.getServiceClassName());
		List<Method> methods = ReflectUtils.findMethodsByName(serviceClass, dreq.getServiceMethodName());
		if(methods.size()>1){
			throw new BaseException("not only one method: " + dreq.getServiceMethodName());
		}
		Method method = methods.get(0);
		System.out.println("method: " + method);
//		RpcMethodResolver rpcMethod = new RpcMethodResolver(method);
		
//		req = jp.test(jsonstr);
		jp.parseParams(method);
		System.out.println("JsonRpcParamsRequest:" + parsedReq);
		System.out.println("JsonRpcParamsRequest:" + JsonMapper.DEFAULT_MAPPER.toJson(parsedReq));
		Assert.assertEquals(req.getJsonrpc(), parsedReq.getJsonrpc());
		Assert.assertEquals(req.getMethod(), parsedReq.getMethod());
//		Object result = ReflectUtils.invokeMethod(method, ReflectUtils.newInstance(serviceClass));
//		System.out.println("args: " + LangUtils.toString(args));
	}

}
