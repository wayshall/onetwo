package appweb.admin.model.service;

import org.junit.Test;
import org.onetwo.plugins.jsonrpc.client.proxy.RpcClientFacotry;

public class JsonRpcTest {
	
	@Test
	public void test(){
		RpcClientFacotry rf = new RpcClientFacotry.Builder()
							.baseUrl("http://localhost:8080/appweb-admin/jsonrpc/")
							.build();
		String result = rf.create(JsonRpcServiceTest.class).say("jfish-jsonrpc");
		System.out.println("result: " + result);
	}

}
