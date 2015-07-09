package org.onetwo.boot.plugins.permission.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.controller.PluginBaseController;
import org.onetwo.boot.plugins.permission.PermissionManager;
import org.onetwo.boot.plugins.permission.entity.IPermission;
import org.onetwo.common.exception.NoAuthorizationException;
import org.onetwo.common.web.csrf.CsrfValid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

@Controller
@RequestMapping("/plugins/permission")
public class PermissionController extends PluginBaseController {

	@Resource
	private PermissionManager<? extends IPermission<?>> permissionManager;
	
//	@Resource
//	private SpringBootConfig springBootConfig;
	
	private void checkAvailable() throws ModelAndViewDefiningException{
		if(!getBootSiteConfig().isProduct())
			return ;
		if(getCurrentLoginUser()==null || !getCurrentLoginUser().isSystemRootUser())
			throw new NoAuthorizationException("无权访问！");
	}
	@RequestMapping(value="index")
	public ModelAndView index(HttpServletResponse response) throws ModelAndViewDefiningException{
		this.checkAvailable();
		IPermission<?> menu = this.permissionManager.getMemoryRootMenu();
		logger.info("menu:\n {}", menu.toTreeString("\n"));
		return pluginMv("/permission/index", "menu", menu, "menuHtml", menu.toTreeString("<br/>"));
	}
	
	@CsrfValid(false)
	@RequestMapping(value="syncmenus", method=RequestMethod.POST)
	public ModelAndView synMenus() throws ModelAndViewDefiningException{
		this.checkAvailable();
//		permissionManager.build();
		this.permissionManager.syncMenuToDatabase();
		IPermission<?> menu = this.permissionManager.getMemoryRootMenu();
		return pluginMv("/permission/index", "menu", menu, "menuHtml", menu.toTreeString("<br/>"), MESSAGE, "同步成功！");
	}
}
