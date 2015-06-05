package org.onetwo.plugins.jsonrpc.test;

import org.onetwo.common.jsonrpc.protocol.JsonRpcRequest;

public interface RpcUserServiceTest {
	
	/*public NamedParamsRequest findById(Long id);
	public NamedParamsRequest findByUserNameAndAge(String userName, int age);
	

	
	public ListParamsRequest findById4List(Long id);
	public ListParamsRequest findByUserNameAndAge4List(String userName, int age);
	*/

	public JsonRpcRequest findById4Object(Long id);
	public JsonRpcRequest save4Object(String userName, RpcUserVo user);

}
