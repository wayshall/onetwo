package org.onetwo.webapp.oauth2.authorization.controller;

import org.onetwo.common.data.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.date.DateUtil;
import org.onetwo.common.spring.web.mvc.utils.WebResultCreator;
import org.onetwo.webapp.oauth2.authorization.vo.UserProfileVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

	@GetMapping("me")
	public SimpleDataResult<?> me(){
		UserProfileVO profile = new UserProfileVO();
		profile.setUserName("testUserName");
		profile.setBirthday(DateUtil.parse("1982-02-02"));
		return WebResultCreator.creator().simple(profile).buildResult();
	}
	
}
