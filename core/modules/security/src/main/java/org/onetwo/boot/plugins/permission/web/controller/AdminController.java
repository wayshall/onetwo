package org.onetwo.boot.plugins.permission.web.controller;

import java.util.Collection;

import javax.annotation.Resource;

import org.onetwo.boot.core.web.controller.PluginBaseController;
import org.onetwo.boot.plugins.permission.service.MenuItemRepository;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.tree.TreeModel;
import org.onetwo.common.web.userdetails.UserDetail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/plugins/permission/admin")
public class AdminController<T extends TreeModel<T>> extends PluginBaseController {

	@Resource
	private MenuItemRepository<T> menuItemRepository;
	
//	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(UserDetail userDetail){
		Collection<T> menus = null;
		if(userDetail==null){
			/*if(bootSiteConfig.isDev())
				menus = menuItemRepository.findAllMenus();
			else
				throw new NotLoginException();*/
			throw new NotLoginException();
		}else if(userDetail.isSystemRootUser()){
			menus = menuItemRepository.findAllMenus();
//			throw new UnsupportedOperationException("not implements yet!");
		}else{
			menus = menuItemRepository.findUserMenus(userDetail);
		}
		
		return pluginMv("/permission/admin", "menus", menus, "adminTitle", getBootSiteConfig().getName());
	}
	
}
