package org.onetwo.boot.plugins.permission.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.plugin.web.PluginBaseController;
import org.onetwo.common.exception.NoAuthorizationException;
import org.onetwo.common.web.csrf.CsrfValid;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.PermissionManager;
import org.onetwo.ext.permission.entity.DefaultIPermission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

@Controller
@RequestMapping("/plugins/permission")
public class PermissionController extends PluginBaseController {

	@Resource
	private PermissionManager<? extends DefaultIPermission<?>> permissionManager;
	
//	@Resource
//	private SpringBootConfig springBootConfig;
	
	private void checkAvailable(UserDetail userDetail) throws ModelAndViewDefiningException{
		if(!getBootSiteConfig().isProduct())
			return ;
		if(userDetail==null || !userDetail.isSystemRootUser())
			throw new NoAuthorizationException("无权访问！");
	}
	@RequestMapping(value="index")
	public ModelAndView index(UserDetail userDetail) throws ModelAndViewDefiningException{
		this.checkAvailable(userDetail);
		DefaultIPermission<?> menu = this.permissionManager.getMemoryRootMenu();
		logger.info("menu:\n {}", menu.toTreeString("\n"));
		return pluginMv("/permission/index", "menu", menu, "menuHtml", menu.toTreeString("<br/>"));
	}
	
	@CsrfValid(false)
	@RequestMapping(value="syncmenus", method=RequestMethod.POST)
	public ModelAndView synMenus(UserDetail userDetail) throws ModelAndViewDefiningException{
		this.checkAvailable(userDetail);
//		permissionManager.build();
		this.permissionManager.syncMenuToDatabase();
		DefaultIPermission<?> menu = this.permissionManager.getMemoryRootMenu();
		return pluginMv("/permission/index", "menu", menu, "menuHtml", menu.toTreeString("<br/>"), MESSAGE, "同步成功！");
	}
}
