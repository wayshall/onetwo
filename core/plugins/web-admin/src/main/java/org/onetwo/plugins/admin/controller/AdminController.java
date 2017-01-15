package org.onetwo.plugins.admin.controller;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.ext.permission.service.MenuItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping("/index")
public class AdminController extends WebAdminBaseController {

	@Resource
	private MenuItemRepository<PermisstionTreeModel> menuItemRepository;
	
//	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(UserDetail userDetail){
		List<PermisstionTreeModel> menus = null;
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
//		menus = menus.get(0).getChildren();
		
		return pluginMv("/admin", "menus", menus, "adminTitle", getBootSiteConfig().getName());
	}
	
}
