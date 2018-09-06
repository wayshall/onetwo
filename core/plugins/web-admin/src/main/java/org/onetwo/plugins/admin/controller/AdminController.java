package org.onetwo.plugins.admin.controller;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.ext.permission.service.MenuItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/****
 * 
 * @author way
 *
 * @param <T>
 * 
 * @Deprecated use web-admin plugin instead of
 */
@Controller
public class AdminController extends WebAdminBaseController {

	@Resource
	private MenuItemRepository<PermisstionTreeModel> menuItemRepository;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView index(UserDetail userDetail){
		List<PermisstionTreeModel> menus = menuItemRepository.findUserMenus(userDetail);
		return pluginMv("/admin", "menus", menus, "adminTitle", getBootSiteConfig().getName());
	}
	
	@RequestMapping(value="/roleRouters", method=RequestMethod.GET)
	@ResponseBody
	public List<PermisstionTreeModel> getRoleRouters(UserDetail userDetail){
		List<PermisstionTreeModel> menus = menuItemRepository.findUserMenus(userDetail);
		return menus;
	}
}
