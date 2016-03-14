package appweb.admin.model.service;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.jsonrpc.client.core.RpcClientFacotry;

import appweb.rpc.service.JsonRpcServiceTest;
import appweb.rpc.vo.UserVo;

public class JsonRpcTest {
	
	@Test
	public void testCommonInvoke(){
		RpcClientFacotry rf = new RpcClientFacotry.Builder()
							.serverEndpoint("http://localhost:8080/appweb-admin/jsonrpc/")
							.build();
		
		JsonRpcServiceTest service = rf.create(JsonRpcServiceTest.class);
		String result = service.say("jsonrpc");
		System.out.println("result: " + result);
		Assert.assertEquals("helllo jsonrpc", result);
		
		Long rs = service.sum(102, 102000);
		Assert.assertEquals(102102L, rs.longValue());
		
		String userName = "jsonrpc";
		int limited = 20;
		List<UserVo> userlist = service.findUsersByUserNameLike(userName, limited);
		System.out.println("userlist: " + JsonMapper.DEFAULT_MAPPER.toJson(userlist));
		Assert.assertNotNull(userlist);
		Assert.assertEquals(limited, userlist.size());
		Assert.assertTrue(userlist.get(0).getUserName().contains(userName));
		

		Page<UserVo> userPage = service.findUsersPageByUserNameLike(userName, limited);
		System.out.println("userPage: " + JsonMapper.DEFAULT_MAPPER.toJson(userPage));
		Assert.assertNotNull(userPage);
		Assert.assertEquals(limited, userPage.getSize());
		Assert.assertTrue(userPage.getResult().get(0).getUserName().contains(userName));
	}

}
