package appweb.admin.model.service;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.jsonrpc.client.proxy.RpcClientFacotry;

import appweb.admin.model.vo.UserVo;

public class JsonRpcTest {
	
	@Test
	public void test(){
		RpcClientFacotry rf = new RpcClientFacotry.Builder()
							.baseUrl("http://localhost:8080/appweb-admin/jsonrpc/")
							.build();
		JsonRpcServiceTest service = rf.create(JsonRpcServiceTest.class);
		String result = service.say("jfish-jsonrpc");
		System.out.println("result: " + result);
		Assert.assertEquals("helllo jfish-jsonrpc", result);
		
		Long rs = service.sum(102, 102000);
		Assert.assertEquals(102102L, rs.longValue());
		
		String userName = "way";
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
