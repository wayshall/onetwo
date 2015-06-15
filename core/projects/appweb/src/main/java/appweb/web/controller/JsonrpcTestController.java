package appweb.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import appweb.rpc.service.JsonRpcServiceTest;

@Controller
@RequestMapping(value="/jsonrpc-test")
public class JsonrpcTestController extends WebBaseController{
	
	@Resource
	private JsonRpcServiceTest jsonRpcServiceTest;

	@RequestMapping("/say")
	@ResponseBody
	public Object say(String something){
		return jsonRpcServiceTest.say(something);
	}

	@RequestMapping("/findUsers")
	@ResponseBody
	public Object findUsersByUserNameLike(String userName, int limited){
		return jsonRpcServiceTest.findUsersByUserNameLike(userName, limited);
	}

	@RequestMapping("/findUserPage")
	@ResponseBody
	public Object findUserPage(String userName, int limited){
		return jsonRpcServiceTest.findUsersPageByUserNameLike(userName, limited);
	}


}
