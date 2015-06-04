package org.onetwo.plugins.jsonrpc.client.test;

import org.onetwo.common.jsonrpc.protocol.JsonRpcParamsRequest;
import org.onetwo.common.jsonrpc.protocol.ListParamsRequest;
import org.onetwo.common.jsonrpc.protocol.NamedParamsRequest;

public interface RpcUserServiceTest {
	
	public NamedParamsRequest findById(Long id);
	public NamedParamsRequest findByUserNameAndAge(String userName, int age);
	

	
	public ListParamsRequest findById4List(Long id);
	public ListParamsRequest findByUserNameAndAge4List(String userName, int age);
	

	public JsonRpcParamsRequest findById4Object(Long id);
	public JsonRpcParamsRequest save4Object(String userName, RpcUserVo user);

}
