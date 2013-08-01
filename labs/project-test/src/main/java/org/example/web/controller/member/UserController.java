package org.example.web.controller.member;


import javax.validation.Valid;

import org.example.model.member.entity.UserEntity;
import org.example.model.member.service.impl.UserServiceImpl;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.utils.Page;
import org.onetwo.plugins.permission.anno.ByMenu;
import org.onetwo.plugins.permission.anno.ByPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/member/user")
@Controller
@ByMenu(name="用户管理", parent=ModuleMenuInfo.class)
public class UserController extends AbstractBaseController {
	 
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@ByPermission(name="用户列表")
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<UserEntity> page){
		userServiceImpl.findPage(page);
		return mv("/member/user-index", "page", page);
	}

	@ByMenu(pageElement=true)
	@ByPermission(name="新增用户")
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("user") UserEntity user){
		return mv("/member/user-new");
	}
	
	@ByPermission(code=":_new")
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(String redirectUrl, @Valid @ModelAttribute("user")UserEntity user, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		if(bind.hasErrors()){
			return mv("/member/user-new");
		}
		this.userServiceImpl.save(user);
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("/member/user");
	}
	
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		UserEntity user = this.userServiceImpl.findById(id);
		return mv("/member/user-edit", "user", user);
	}
	

	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("user") @Valid UserEntity user, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			return mv("/member/user-edit");
		}
		this.userServiceImpl.save(user);
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("/member/user/"+user.getId());
	}
	

	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(@RequestParam(value="ids")long[] ids, RedirectAttributes redirectAttributes){
		for(long id : ids){
			this.userServiceImpl.removeById(id);
		}
		addFlashMessage(redirectAttributes, "删除成功！");
		return redirectTo("/member/user");
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id) throws BusinessException{
		UserEntity user =  this.userServiceImpl.findById(id);
		return mv("/member/user-show", "user", user);
	}
	
	
}