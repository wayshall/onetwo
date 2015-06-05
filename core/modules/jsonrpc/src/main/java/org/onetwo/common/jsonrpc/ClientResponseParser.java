package org.onetwo.common.jsonrpc;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.jsonrpc.exception.JsonRpcError;
import org.onetwo.common.jsonrpc.exception.JsonRpcException;
import org.onetwo.common.jsonrpc.protocol.JsonRpcBase.KeyWords;
import org.onetwo.common.jsonrpc.protocol.JsonRpcRequest;
import org.onetwo.common.jsonrpc.protocol.JsonRpcResponse;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.convert.Types;

import com.fasterxml.jackson.databind.JsonNode;

public class ClientResponseParser {
	
//	private RpcMethodResolver rpcMethod;
	private JsonMapper mapper = JsonMapper.DEFAULT_MAPPER;
	private JsonRpcResponse response;
	private JsonNode rootNode;
	
	
	public ClientResponseParser(String jsonstr) {
		super();
		response = new JsonRpcResponse();
		rootNode = mapper.readTree(jsonstr);
	}

	public JsonRpcResponse parseBase(){
		response.setJsonrpc(parseRequiredNode(KeyWords.JSONRPC).asText());
		Optional.ofNullable(rootNode.path(KeyWords.ID)).ifPresent(node->response.setId(node.asLong()==0?null:node.asLong()));
		
		return response;
	}
	
	protected JsonNode parseRequiredNode(String path){
		return Optional.ofNullable(rootNode.path(path))
						.orElseThrow(()->new JsonRpcException(JsonRpcError.INVALID_REQUEST));
	}

	public JsonRpcRequest parseParams(Method method)  {
		RpcMethodResolver rpcMethod = new RpcMethodResolver(method);
		try {
			List<Object> params = parseAsMethodArgs(rpcMethod);
			this.request.setParams(params);
			return request;
		} catch (JsonRpcException e) {
			throw e;
		} catch (Exception e) {
			throw new JsonRpcException(JsonRpcError.INVALID_PARAMS, "parse params error: " + e.getMessage(), e);
		}
	}

	protected List<Object> parseAsMethodArgs(RpcMethodResolver rpcMethod) throws Exception {
		
		JsonNode paramsNode = rootNode.path(KeyWords.PARAMS);
		List<Object> paramList  = LangUtils.newArrayList();
		if(paramsNode==null){
			//没有参数
			
		}else if(paramsNode.isArray()){
			int index = 0;
			for(BaseMethodParameter parameter : rpcMethod.getParameters()){
				JsonNode paramNameNode = Optional.ofNullable(paramsNode.path(index++)).get();
				Object pvalue = parseNode(paramNameNode, parameter);
				paramList.add(pvalue);
			}
			
		}else if(paramsNode.isObject()){
			for(BaseMethodParameter parameter : rpcMethod.getParameters()){
//				JsonNode paramNameNode = path(paramsNode, parameter.getParameterName()).get();
				JsonNode paramNameNode = Optional.ofNullable(paramsNode.path(parameter.getParameterName())).get();
				Object pvalue = parseNode(paramNameNode, parameter);
				paramList.add(parameter.getParameterIndex(), pvalue);
			}
			
		}else{
			throw new JsonRpcException(JsonRpcError.INVALID_PARAMS, "error params : " + paramsNode);
		}
		
//		req.setParams(paramList);
		return paramList;
	}
	
	protected Object parseNode(JsonNode node, BaseMethodParameter parameter) throws Exception{
		Assert.notNull(node);
		Class<?> valueClass = parameter.getParameterType();
		Object value = null;
//		Class<?> valueClass = (Class<?>) valueType;
		if(node.isArray()){
//			List<Object> list = LangUtils.newArrayList();
			if(valueClass.isArray()){
//				value = (Object[])mapper.fromJsonAsArray(node.toString(), valueType);
				/*for (int i = 0; i < node.size(); i++) {
					Object val = parseNode(node, valueClass.getComponentType());
					list.add(val);
				}
				value = list.toArray();*/
				value = mapper.fromJsonAsElementArray(node.toString(), valueClass.getComponentType());
			}else{
				/*valueClass = ReflectUtils.getGenricType(valueClass, 0);
				for (int i = 0; i < node.size(); i++) {
					Object val = parseNode(node, valueClass.getComponentType());
					list.add(val);
				}
				value = list;*/
				Class<?> elementClass = ReflectUtils.getGenricType(parameter.getGenericParameterType(), 0);
				value = mapper.fromJsonAsList(node.toString(), elementClass);
			}
		}else if(node.isObject()){
			value = mapper.fromJson(node.toString(), valueClass);
		}else if(node.isBinary()){
			value = node.binaryValue();
		}else if(node.isNull()){
			value = null;
		}else{
			value = Types.convertValue(node.asText(), valueClass);
		}
		return value;
	}

	/*protected Optional<JsonNode> path(JsonNode node, String path){
		return Optional.ofNullable(node.path(path));
	}*/
	
	/*protected Optional<JsonNode> path(JsonNode node, int index){
		JsonNode pathNode = node.path(index);
		return Optional.ofNullable(pathNode);
	}*/

	public JsonRpcRequest getRequest() {
		return request;
	}

}
