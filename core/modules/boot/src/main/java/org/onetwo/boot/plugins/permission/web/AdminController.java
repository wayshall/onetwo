package org.onetwo.boot.plugins.permission.web;

import java.util.Collection;

import javax.annotation.Resource;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.controller.PluginBaseController;
import org.onetwo.boot.plugins.permission.service.MenuItemRepository;
import org.onetwo.common.utils.TreeModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/plugins/permission/admin")
public class AdminController<T extends TreeModel<T>> extends PluginBaseController {

	@Resource
	private MenuItemRepository<T> menuItemRepository;
	
	@Resource
	private BootSiteConfig bootSiteConfig;
	
//	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(){
		Collection<T> menus = null;
		/*if(userDetail==null){
			if(bootSiteConfig.isDev())
				menus = menuItemRepository.findAllMenus();
			else
				throw new NotLoginException();
		}else{
			throw new UnsupportedOperationException("not implements yet!");
//			menus = menuItemRepository.findUserMenus(userDetail);
		}*/
		if(bootSiteConfig.isDev())
			menus = menuItemRepository.findAllMenus();
		
		return pluginMv("/permission/admin", "menus", menus, "adminTitle", bootSiteConfig.getAppName());
	}
	
}
