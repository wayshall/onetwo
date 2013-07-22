package org.onetwo.plugins.fmtag.controller;

import org.onetwo.common.spring.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/index")
public class FmtagIndexController extends BaseController<Object>{
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(){
		return indexView();
	}

}
