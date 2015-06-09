package org.onetwo.plugins.jsonrpc.server.core;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.jsonrpc.ServerRequestParser;
import org.onetwo.common.jsonrpc.exception.JsonRpcError;
import org.onetwo.common.jsonrpc.exception.JsonRpcException;
import org.onetwo.common.jsonrpc.protocol.DispatchableMethod;
import org.onetwo.common.jsonrpc.protocol.JsonRpcRequest;
import org.onetwo.common.jsonrpc.protocol.JsonRpcResponse;
import org.onetwo.common.jsonrpc.protocol.JsonRpcResponseError;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.web.csrf.CsrfValid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
@RequestMapping(value="*", method=RequestMethod.POST)
public class DispatcherController extends PluginSupportedController {
	
	@Resource
	private JsonRpcSerivceRepository jsonRpcSerivceRepository;
	
	@CsrfValid(false)
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Object dispatcher(@RequestBody String jsonString){
		logger.info("req json:\n{}", jsonString);
		JsonRpcResponse response = new JsonRpcResponse();
		ServerRequestParser jp = null;
		try {
			jp = createJsonRpcParser(jsonString);
			JsonRpcRequest parsedReq = jp.parseBase().getRequest();
			
			DispatchableMethod dispatchableMethod = new DispatchableMethod(parsedReq.getMethod());
			Object result = dispathcerAndInvokeMethod(jp, dispatchableMethod);
			if(parsedReq.getId()==null){
				//id=null为通知，无需返回响应对象
				return null;
			}
			response.setResult(result);
			response.setId(jp.getRequest().getId());
			//成功时，必须不包含error对象，mapper注意忽略null值
		} catch (JsonRpcException e) {
			logger.error("jsonrpc error.", e);
			JsonRpcResponseError error = JsonRpcResponseError.create(e.getJsonRpcError(), e.getMessage());
			response.setError(error);
			
		} catch (Exception e) {
			logger.error("system error.", e);
			JsonRpcResponseError error = JsonRpcResponseError.create(JsonRpcError.SERVER_ERROR, e.getMessage());
			response.setError(error);
		}
//		return mv("", SingleReturnWrapper.wrap(response));
		return response;
	}

	private ServerRequestParser createJsonRpcParser(String jsonString){
		ServerRequestParser jp = new ServerRequestParser(jsonString);
		return jp;
	}

	private Object dispathcerAndInvokeMethod(ServerRequestParser jp, DispatchableMethod dispatchableMethod){
//		JsonRpcParser jp = new JsonRpcParser(jsonString);
//		JsonRpcParamsRequest parsedReq = jp.parseHeader();
//		DispatchableMethod dispatchableMethod = new DispatchableMethod(parsedReq.getMethod());
		
		Class<?> serviceClass = ReflectUtils.loadClass(dispatchableMethod.getServiceClassName());
		Object bean = jsonRpcSerivceRepository.findService(dispatchableMethod.getServiceClassName());
		if(bean==null){
			throw new JsonRpcException(JsonRpcError.INTERNAL_ERROR, "no service found: " + serviceClass);
		}

		/*List<?> beans = SpringUtils.getBeans(applicationContext, serviceClass);
		if(LangUtils.isEmpty(beans)){
			throw new JsonRpcException(JsonRpcError.INTERNAL_ERROR, "no service found: " + serviceClass);
		}
		if(beans.size()>1){
			throw new JsonRpcException(JsonRpcError.INTERNAL_ERROR, "greater one service found: " + serviceClass);
		}
		Object bean = beans.get(0);
		*/
		
		List<Method> methods = ReflectUtils.findMethodsByName(serviceClass, dispatchableMethod.getServiceMethodName());
		if(methods.size()>1){
			throw new JsonRpcException(JsonRpcError.INTERNAL_ERROR, "not only one method: " + dispatchableMethod);
		}
		Method method = methods.get(0);
		logger.info("dispatchableMethod: {}", dispatchableMethod);
		jp.parseParams(method);
		JsonRpcRequest req = jp.getRequest();
		
		Object result = null;
		try {
			result = ReflectUtils.invokeMethod(method, bean, req.getParams(List.class).toArray());
//			response.setResult(result);
			return result;
		} catch (Exception e) {
			logger.error("invoke jsonrpc method error: " + e.getMessage(), e);
			throw new JsonRpcException(JsonRpcError.SERVER_ERROR, e);
		}
	}

}
