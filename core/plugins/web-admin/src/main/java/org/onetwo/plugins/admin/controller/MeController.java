package org.onetwo.plugins.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController extends WebAdminBaseController {
	
	@GetMapping("me")
	public Object me(){
		return this.getCurrentLoginUser();
	}

}
