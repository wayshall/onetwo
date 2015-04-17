package qingxinkd.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController extends WebBaseController{
	

	@RequestMapping("/index")
	public ModelAndView index(){
		return mv("index");
	}


}
