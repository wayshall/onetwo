package org.onetwo.plugins.jsonrpc.client.test;

import org.onetwo.common.jsonrpc.protocol.NamedParamsRequest;

public interface RpcUserServiceTest {
	
	public NamedParamsRequest findById(Long id);
	
	public NamedParamsRequest findByUserNameAndAge(String userName, int age);

}
