package appweb.admin.model.service;

import org.onetwo.plugins.jsonrpc.server.annotation.JsonRpcService;

@JsonRpcService
public interface JsonRpcServiceTest {
	public String say(String something);
}