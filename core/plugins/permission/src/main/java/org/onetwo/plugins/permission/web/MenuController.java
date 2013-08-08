package org.onetwo.plugins.permission.web;

import javax.annotation.Resource;

import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.plugins.permission.entity.MenuEntity;
import org.onetwo.plugins.permission.service.PermissionManagerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MenuController extends AbstractBaseController {

	@Resource
	private PermissionManagerImpl permissionManagerImpl;
	
	@RequestMapping(value="index")
	public ModelAndView index(){
		MenuEntity menu = this.permissionManagerImpl.getMenuInfoParser().getRootMenu();
		return mv("menu-index", "menu", menu);
	}
	
	@RequestMapping(value="syncmenus", method=RequestMethod.POST)
	public ModelAndView synMenus(){
		this.permissionManagerImpl.syncMenuToDatabase();
		MenuEntity menu = this.permissionManagerImpl.getMenuInfoParser().getRootMenu();
		return mv("menu-index", "menu", menu, MESSAGE, "同步成功！");
	}
}
