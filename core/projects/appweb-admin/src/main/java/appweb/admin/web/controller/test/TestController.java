package appweb.admin.web.controller.test;

import java.util.Map;

import javax.annotation.Resource;

import org.onetwo.common.utils.LangUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import appweb.admin.model.service.impl.TestServiceImpl;
import appweb.admin.web.controller.WebBaseController;

@Controller
@RequestMapping("test")
public class TestController extends WebBaseController {

	@Resource
	private TestServiceImpl testServiceImpl;
	
	
	@RequestMapping("sayTo")
	@ResponseBody
	public String sayTo(String userName){
		return "Hello " + userName;
	}

	@RequestMapping("work")
	@ResponseBody
	public Map<?, ?> work(String userName, String workCity){
		return LangUtils.asMap("userName", userName, "workCity", workCity);
	}

//	@RequestMapping(value="error", produces="application/json")
	@RequestMapping(value="error")
	@ResponseBody
	public String error(){
		return this.testServiceImpl.invokeError();
	}
	
}
