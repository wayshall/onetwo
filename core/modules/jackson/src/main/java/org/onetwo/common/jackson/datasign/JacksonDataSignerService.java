package org.onetwo.common.jackson.datasign;

import java.util.Map;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.jackson.exception.JsonException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.DataSigner.DefaultDataSigner;
import org.onetwo.common.utils.ParamUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonDataSignerService extends DefaultDataSigner {
	private JsonMapper jsonMapper = JsonMapper.IGNORE_NULL;
	private String arrayNodeJoner = "&";
	

	public JacksonDataSignerService() {
	}
	
	public JacksonDataSignerService(String arrayNodeJoner) {
		super();
		this.arrayNodeJoner = arrayNodeJoner;
	}

//	protected Map<String, Object> toMap(Object params, String... excludeProperties) {
//		Map<String, Object> res = null;
//		if (params instanceof ObjectNode) {
//			res = jsonMapper.getObjectMapper().convertValue(params, Map.class);
//		} else if (params instanceof ArrayNode) {
//			res = jsonMapper.getObjectMapper().convertValue(params, List.class);
//		} else {
//			throw new JsonException("error json data: " + params);
//		}
//		return res;
//	}
	
	protected String convertToSourceString(SigningData data, String... excludeProperties){
		Assert.hasText(data.getSecretkey(), "secretkey can not blank");
//		Map<String, Object> requestMap = toMap(data.getParams(), excludeProperties);
//		final String paramString = ParamUtils.comparableKeyMapToParamString(requestMap);
		
		String paramString = null;
		Object params = data.getParams();
		if (params instanceof ObjectNode) {
			paramString = convertObjectNodeToString((ObjectNode)params);
		} else if (params instanceof ArrayNode) {
			paramString = convertArrayNodeToString((ArrayNode)params);
		} else {
			throw new JsonException("error json data: " + params);
		}
		
		String sourceString = data.getSecretkey() + paramString + data.getTimestamp();
		if(data.isDebug()){
			logger.info("param string: {}", paramString);
			logger.info("source string: {}", sourceString);
		}
		return sourceString;
	}

	@SuppressWarnings("unchecked")
	protected String convertObjectNodeToString(ObjectNode jsonNode) {
		Map<String, Object> requestMap = jsonMapper.getObjectMapper().convertValue(jsonNode, Map.class);
		String paramString = ParamUtils.comparableKeyMapToParamString(requestMap);
		return paramString;
	}

	protected String convertArrayNodeToString(ArrayNode arrayNode) {
//		nodeList.sort(Comparator.<JsonNode, Comparable>comparing(node -> (Comparable)node.get("sortFieldName")));
		String paramString = "";
		int index = 0;
		for (JsonNode node : arrayNode) {
			String nodeString = convertObjectNodeToString((ObjectNode)node);
			if (index==0) {
				paramString = nodeString;
			} else {
				paramString = paramString + arrayNodeJoner + nodeString;
			}
			index++;
		}
		return paramString;
	}
	
}
