package org.onetwo.plugins.jsonrpc;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.jsonrpc.JsonRpcParser;
import org.onetwo.common.jsonrpc.exception.JsonRpcException;
import org.onetwo.common.jsonrpc.protocol.DispatchableMethod;
import org.onetwo.common.jsonrpc.protocol.JsonRpcParamsRequest;
import org.onetwo.common.jsonrpc.protocol.JsonRpcResponse;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class DispatcherController extends PluginSupportedController {
	
	@Resource
	private ApplicationContext applicationContext;
	
	@RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
	public Object dipatcher(@RequestBody String jsonString){
		JsonRpcParser jp = new JsonRpcParser(jsonString);
		JsonRpcParamsRequest parsedReq = jp.parseHeader();
		DispatchableMethod dispatchableMethod = new DispatchableMethod(parsedReq.getMethod());
		
		Class<?> serviceClass = ReflectUtils.loadClass(dispatchableMethod.getServiceClassName());

		List<?> beans = SpringUtils.getBeans(applicationContext, serviceClass);
		if(LangUtils.isEmpty(beans)){
			throw new JsonRpcException("no service found: " + serviceClass);
		}
		if(beans.size()>1){
			throw new JsonRpcException("greater one service found: " + serviceClass);
		}
		
		List<Method> methods = ReflectUtils.findMethodsByName(serviceClass, dispatchableMethod.getServiceMethodName());
		if(methods.size()>1){
			throw new BaseException("not only one method: " + parsedReq.getMethod());
		}
		Method method = methods.get(0);
		logger.info("dispatchableMethod: {}", dispatchableMethod);
		JsonRpcParamsRequest req = jp.parseParams(method);
		
		Object bean = beans.get(0);
		Object result = ReflectUtils.invokeMethod(method, bean);
		JsonRpcResponse response = new JsonRpcResponse();a
		return response;
	}

}
