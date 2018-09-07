package org.onetwo.plugins.admin.controller;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.tree.TreeBuilder;
import org.onetwo.common.utils.func.Closure1;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.ext.permission.PermissionManager;
import org.onetwo.ext.permission.api.IPermission;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.ext.permission.service.MenuItemRepository;
import org.onetwo.ext.permission.utils.PermissionUtils;
import org.onetwo.plugins.admin.entity.AdminPermission;
import org.onetwo.plugins.admin.service.impl.AdminMenuItemServiceImpl;
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
	@Resource
	private PermissionManager<?> permissionManager;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public ModelAndView index(UserDetail userDetail){
		List<PermisstionTreeModel> menus = menuItemRepository.findUserMenus(userDetail);
		return pluginMv("/admin", "menus", menus, "adminTitle", getBootSiteConfig().getName());
	}
	
	@RequestMapping(value="/roleRouters", method=RequestMethod.GET)
	@ResponseBody
	public List<PermisstionTreeModel> getRoleRouters(UserDetail userDetail){
		List<PermisstionTreeModel> menus = menuItemRepository.findUserPermissions(userDetail, (userPerms, allPerms)->{
			TreeBuilder<PermisstionTreeModel> treebuilder = PermissionUtils.createMenuTreeBuilder(userPerms);
			treebuilder.buidTree(node->{
				AdminPermission p = (AdminPermission)allPerms.get(node.getParentId());
				return AdminMenuItemServiceImpl.convertToMenuTreeModel(p);
			});
			return treebuilder.getRootNodes();
		});
		
		return menus;
	}

	String buildTreeString(PermisstionTreeModel permission){
		final StringBuilder str = new StringBuilder();
		PermissionUtils.buildString(str, permission, "--", new Closure1<PermisstionTreeModel>() {
			
			@Override
			public void execute(IPermission obj) {
				if(PermissionUtils.isMenu(obj)){
					str.append(obj.getName()).append("(").append(obj.getCode()).append(")");
					str.append(":").append(obj.getUrl()==null?"":obj.getUrl() );
				}else{
					str.append(obj.getName()).append("(").append(obj.getCode()).append(")");
				}
				str.append(" -> ").append(obj.getResourcesPattern()==null?"":obj.getResourcesPattern() );
				str.append(spliter);
			}
		});
		return str.toString();
	}
}
