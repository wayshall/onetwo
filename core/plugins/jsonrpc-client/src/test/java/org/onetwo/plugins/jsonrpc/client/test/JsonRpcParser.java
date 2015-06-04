package org.onetwo.plugins.jsonrpc.client.test;

import java.lang.reflect.Method;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.jsonrpc.protocol.JsonRpcParamsRequest;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.convert.Types;
import org.onetwo.plugins.jsonrpc.client.proxy.RpcMethodResolver;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonRpcParser {
	
	private RpcMethodResolver rpcMethod;
	private JsonMapper mapper = JsonMapper.DEFAULT_MAPPER;
	
	
	public JsonRpcParser(Method rpcMethod) {
		super();
		this.rpcMethod = new RpcMethodResolver(rpcMethod);
	}


	public JsonRpcParamsRequest parseAsMethodArgs(String jsonstr) throws Exception {
		JsonRpcParamsRequest req = new JsonRpcParamsRequest();
		JsonNode rootNode = mapper.readTree(jsonstr);
		req.setJsonrpc(path(rootNode, "jsonrpc").asText());
		req.setId(path(rootNode, "id").asLong());
		req.setMethod(path(rootNode, "method").asText());
		
		JsonNode paramsNode = rootNode.path("params");
		List<Object> paramList  = LangUtils.newArrayList();
		if(paramsNode==null){
			//通知
			
		}else if(paramsNode.isArray()){
			int index = 0;
			for(BaseMethodParameter parameter : rpcMethod.getParameters()){
				JsonNode paramNameNode = path(paramsNode, index++);
				Object pvalue = parseNode(paramNameNode, parameter.getParameterType());
				paramList.add(pvalue);
			}
			
		}else if(paramsNode.isObject()){
			for(BaseMethodParameter parameter : rpcMethod.getParameters()){
				JsonNode paramNameNode = path(paramsNode, parameter.getParameterName());
				Object pvalue = parseNode(paramNameNode, parameter.getParameterType());
				paramList.add(parameter.getParameterIndex(), pvalue);
			}
			
		}else{
			throw new BaseException("error params : " + paramsNode);
		}
		
		req.setParams(paramList);
		
		return req;
	}
	
	protected Object parseNode(JsonNode node, Class<?> valueClass) throws Exception{
		Assert.notNull(node);
		Object value = null;
//		Class<?> valueClass = (Class<?>) valueType;
		if(node.isArray()){
			List<Object> list = LangUtils.newArrayList();
			if(valueClass.isArray()){
//				value = (Object[])mapper.fromJsonAsArray(node.toString(), valueType);
				for (int i = 0; i < node.size(); i++) {
					Object val = parseNode(node, valueClass.getComponentType());
					list.add(val);
				}
				value = list.toArray();
			}else{
				valueClass = ReflectUtils.getGenricType(valueClass, 0);
				for (int i = 0; i < node.size(); i++) {
					Object val = parseNode(node, valueClass.getComponentType());
					list.add(val);
				}
				value = list;
			}
		}else if(node.isObject()){
			value = mapper.fromJson(node.toString(), valueClass);
		}else if(node.isBinary()){
			value = node.binaryValue();
		}else{
			value = Types.convertValue(node.asText(), valueClass);
		}
		return value;
	}
	
	protected JsonNode path(JsonNode node, String path){
		JsonNode pathNode = node.path(path);
		Assert.notNull(pathNode);
		return pathNode;
	}
	
	protected JsonNode path(JsonNode node, int index){
		JsonNode pathNode = node.path(index);
		Assert.notNull(pathNode);
		return pathNode;
	}

}
