package qy.iccard.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import qy.iccard.entity.UserEntity;
import qy.iccard.service.UserServiceImpl;

@RequestMapping("/user")
@Controller
public class UserController {
	 
	@Autowired
	private UserServiceImpl userServiceImpl;
	

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(){
		List<UserEntity> users = userServiceImpl.findAll();
		ModelAndView mv = new ModelAndView("user-index");
		mv.addObject("users", users);
		return mv;
	}
	
	
	
}