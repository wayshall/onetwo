package org.onetwo.webapp.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator;
import org.onetwo.plugins.admin.entity.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/rest")
public class TestRestController {
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping("say")
	public SimpleDataResult<?> say(HttpServletRequest request, String word){
		System.out.println("this.request:"+this.request.getParameter("word"));
		System.out.println("request:"+request.getParameter("word"));
		if("error".equals(word)){
			throw new ServiceException("error word!");
		}
		return WebResultCreator.creator().simple(word).buildResult();
	}
	
	@RequestMapping("echo")
	public SimpleDataResult<?> echo(@ModelAttribute("adminUser") AdminUser user){
		return WebResultCreator.creator().simple(user).buildResult();
	}
	
	@ModelAttribute("adminUser")
	public AdminUser getAdminUser(HttpServletRequest request){
		String nickName = request.getParameter("test");
		AdminUser user = new AdminUser();
		if(StringUtils.isNotBlank(nickName)){
			user.setNickName("testNickName");
		}
		return user;
	}
	
	@ExceptionHandler
	public SimpleDataResult<?> exceptionHandler(Throwable e){
		return WebResultCreator.creator().error(e.getMessage()).buildResult();
	}

}
