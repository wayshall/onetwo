package org.onetwo.plugins.jsonrpc.client.test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jackson.JsonMapper;
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
		
		int lastDot = req.getMethod().lastIndexOf('.');
		String className = "";
		String methodName = "";
		if(lastDot!=-1){
			className = req.getMethod().substring(0, lastDot);
			methodName = req.getMethod().substring(lastDot+1);
		}else{
			throw new BaseException("error method name: " + req.getMethod());
		}
		System.out.println("class: " + className);
		System.out.println("method: " + methodName);
		
		Class<?> serviceClass = ReflectUtils.loadClass(className);
		List<Method> methods = ReflectUtils.findMethodsByName(serviceClass, methodName);
		if(methods.size()>1){
			throw new BaseException("not only one method: " + methodName);
		}
		Method method = methods.get(0);
		System.out.println("method: " + method);
//		RpcMethodResolver rpcMethod = new RpcMethodResolver(method);
		
		JsonRpcParser jp = new JsonRpcParser(method);
//		req = jp.test(jsonstr);
		req = jp.parseAsMethodArgs(jsonstr);
		System.out.println("JsonRpcParamsRequest:" + req);
		System.out.println("JsonRpcParamsRequest:" + JsonMapper.DEFAULT_MAPPER.toJson(req));
//		Object result = ReflectUtils.invokeMethod(method, ReflectUtils.newInstance(serviceClass));
//		System.out.println("args: " + LangUtils.toString(args));
	}

}
