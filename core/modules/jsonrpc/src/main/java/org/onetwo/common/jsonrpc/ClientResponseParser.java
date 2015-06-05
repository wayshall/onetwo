package org.onetwo.common.jsonrpc;

import java.util.Optional;

import org.onetwo.common.jsonrpc.exception.JsonRpcError;
import org.onetwo.common.jsonrpc.exception.JsonRpcException;
import org.onetwo.common.jsonrpc.protocol.JsonRpcBase.KeyWords;
import org.onetwo.common.jsonrpc.protocol.JsonRpcResponse;
import org.onetwo.common.jsonrpc.protocol.JsonRpcResponseError;

import com.fasterxml.jackson.databind.JsonNode;

public class ClientResponseParser extends AbstractJsonParser {
	
	private JsonRpcResponse response;
	private JsonNode rootNode;
	
	
	public ClientResponseParser(String jsonstr) {
		super();
		response = new JsonRpcResponse();
		rootNode = mapper.readTree(jsonstr);
	}

	public ClientResponseParser parseBase(){
		response.setJsonrpc(parseRequiredNode(KeyWords.JSONRPC).asText());
		Optional.ofNullable(rootNode.path(KeyWords.ID))
				.ifPresent(node->response.setId(node.asLong()==0?null:node.asLong()));
		
		Optional.ofNullable(rootNode.path(KeyWords.ERROR))
				.ifPresent(node->{
					JsonRpcResponseError error = mapper.fromJson(node.toString(), JsonRpcResponseError.class);
					response.setError(error);
				});
		return this;
	}
	
	protected JsonNode parseRequiredNode(String path){
		return Optional.ofNullable(rootNode.path(path))
						.orElseThrow(()->new JsonRpcException(JsonRpcError.INVALID_RESPONSE));
	}

	public ClientResponseParser parseResult(RpcMethodResolver rpcMethod)  {
//		RpcMethodResolver rpcMethod = new RpcMethodResolver(method);
		try {
			JsonNode resultNode = rootNode.path(KeyWords.RESULT);
			if(resultNode==null){
				return this;
			}
			Object result = parseResult(resultNode, rpcMethod);
			response.setResult(result);
			return this;
		} catch (JsonRpcException e) {
			throw e;
		} catch (Exception e) {
			throw new JsonRpcException(JsonRpcError.PARSE_ERROR, "parse result error: " + e.getMessage(), e);
		}
	}

	protected Object parseResult(JsonNode resultNode, RpcMethodResolver rpcMethod) throws Exception {
		Object result = null;
		if(rpcMethod.isGenericReturnType()){
			result = parseNode(resultNode, rpcMethod.getMethod().getGenericReturnType());
		}else{
			result = parseNode(resultNode, rpcMethod.getResponseType());
		}
		return result;
	}

	public JsonRpcResponse getResponse() {
		return response;
	}

}
