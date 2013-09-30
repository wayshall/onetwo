package org.example.web.controller;

import org.onetwo.common.spring.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("rawtypes")
@Controller
public class IndexController extends BaseController{
	
	@RequestMapping("/index")
	public ModelAndView index(){
		return mv("index");
	}

}
