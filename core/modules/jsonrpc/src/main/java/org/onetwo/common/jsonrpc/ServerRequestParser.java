package org.onetwo.common.jsonrpc;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.jsonrpc.exception.JsonRpcError;
import org.onetwo.common.jsonrpc.exception.JsonRpcException;
import org.onetwo.common.jsonrpc.protocol.JsonRpcBase.KeyWords;
import org.onetwo.common.jsonrpc.protocol.JsonRpcRequest;
import org.onetwo.common.proxy.BaseMethodParameter;
import org.onetwo.common.utils.LangUtils;

import com.fasterxml.jackson.databind.JsonNode;

public class ServerRequestParser extends AbstractJsonParser {
	
//	private RpcMethodResolver rpcMethod;
	private JsonRpcRequest request;
	private JsonNode rootNode;
	
	
	public ServerRequestParser(String jsonstr) {
		super(JsonMapper.IGNORE_NULL);
		request = new JsonRpcRequest();
		rootNode = mapper.readTree(jsonstr);
	}

	public ServerRequestParser parseBase(){
		request.setJsonrpc(parseRequiredNode(KeyWords.JSONRPC).asText());
		Optional.ofNullable(rootNode.path(KeyWords.ID)).ifPresent(node->request.setId(node.asLong()==0?null:node.asLong()));
		request.setMethod(parseRequiredNode(KeyWords.METHOD).asText());
		
		return this;
	}
	
	protected JsonNode parseRequiredNode(String path){
		return Optional.ofNullable(rootNode.path(path))
						.orElseThrow(()->new JsonRpcException(JsonRpcError.INVALID_REQUEST));
	}

	public ServerRequestParser parseParams(Method method)  {
		RpcMethodResolver rpcMethod = new RpcMethodResolver(method);
		try {
			List<Object> params = parseAsMethodArgs(rpcMethod);
			this.request.setParams(params);
			return this;
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
		return parseNode(node, parameter.getParameterType());
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
