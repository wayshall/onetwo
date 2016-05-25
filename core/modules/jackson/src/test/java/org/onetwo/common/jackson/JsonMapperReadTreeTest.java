package org.onetwo.common.jackson;


import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapperReadTreeTest {

	@Test
	public void testReadTree() throws Exception {
		String content = "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById\",\"params\":{\"id\":1}}";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(content);
		JsonNode paramsNode = rootNode.path("params");
		Assert.assertTrue(paramsNode.isObject());
		
		content = "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById4List\",\"params\":[1]}";
		rootNode = mapper.readTree(content);
		paramsNode = rootNode.path("params");
		System.out.println("params is array: " + paramsNode.isArray());
		System.out.println("params is object: " + paramsNode.isObject());
		Assert.assertTrue(paramsNode.isArray());
		Assert.assertFalse(paramsNode.isObject());
	}
	


	/*@Test
	public void testReadTreeForReader() throws Exception {
		String content = "{\"jsonrpc\":\"2.0\",\"id\":null,\"method\":\"org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById\",\"params\":{\"id\":1}}";
		InputStream in = new StringBufferInputStream(content);
		
		JsonNode rootNode = JsonMapper.DEFAULT_MAPPER.readTree(in);
		JsonNode paramsNode = rootNode.path("params");
		Assert.assertTrue(paramsNode.isObject());
		System.out.println("str:"+rootNode.textValue());
		
		NamedParamsRequest fromJsonReq = JsonMapper.DEFAULT_MAPPER.getObjectMapper().readValue(rootNode, NamedParamsRequest.class);
		Assert.assertEquals("2.0", fromJsonReq.getJsonrpc());
		Assert.assertEquals("org.onetwo.plugins.jsonrpc.client.test.RpcUserServiceTest.findById", fromJsonReq.getMethod());
		Assert.assertTrue(fromJsonReq.getParams().containsKey("id"));
		Assert.assertEquals(1L, fromJsonReq.getParams().get("id"));
		
	}*/
}
