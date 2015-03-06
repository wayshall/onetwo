package org.onetwo.plugins.permission.web;

import javax.annotation.Resource;

import org.onetwo.common.exception.NoAuthorizationException;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.csrf.CsrfValid;
import org.onetwo.plugins.permission.entity.IMenu;
import org.onetwo.plugins.permission.service.PermissionManagerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

@Controller
public class MenuController extends PluginSupportedController {

	@Resource
	private PermissionManagerImpl permissionManagerImpl;
	
	
	private void checkAvailable() throws ModelAndViewDefiningException{
		if(!BaseSiteConfig.getInstance().isProduct())
			return ;
		if(getCurrentLoginUser()==null || !getCurrentLoginUser().isSystemRootUser())
			throw new NoAuthorizationException("无权访问！");
	}
	@RequestMapping(value="index")
	public ModelAndView index() throws ModelAndViewDefiningException{
		this.checkAvailable();
		IMenu menu = this.permissionManagerImpl.getMenuInfoParser().getRootMenu();
		return pluginMv("menu-index", "menu", menu);
	}
	
	@CsrfValid(false)
	@RequestMapping(value="syncmenus", method=RequestMethod.POST)
	public ModelAndView synMenus() throws ModelAndViewDefiningException{
		this.checkAvailable();
		this.permissionManagerImpl.syncMenuToDatabase();
		IMenu menu = this.permissionManagerImpl.getMenuInfoParser().getRootMenu();
		return pluginMv("menu-index", "menu", menu, MESSAGE, "同步成功！");
	}
}
