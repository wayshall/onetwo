package org.onetwo.plugins.admin.controller;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.easyui.EasyChildrenTreeModel;
import org.onetwo.easyui.EasyModel;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.plugins.admin.AdminModule.AdminRoleMgr.AssignPermission;
import org.onetwo.plugins.admin.entity.AdminPermission;
import org.onetwo.plugins.admin.service.impl.AdminRoleServiceImpl;
import org.onetwo.plugins.admin.service.impl.PermissionManagerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("rolePermission")
@Controller
public class RolePermissionController extends WebAdminBaseController {
	@Resource
	private AdminRoleServiceImpl adminRoleServiceImpl;
	@Resource
	private PermissionManagerImpl permissionManagerImpl;
	

	@ByPermissionClass(AssignPermission.class)
	@RequestMapping(value="{roleId}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("roleId") long roleId){
		List<String> rolePerms = this.adminRoleServiceImpl.findAppPermissionCodesByRoleIds(null, roleId);
		List<AdminPermission> perms = permissionManagerImpl.findAppPermissions(null);
		EasyChildrenTreeModel treeModel = EasyModel.newChildrenTreeBuilder(AdminPermission.class)
				 .mapId("code")
				 .mapText("name")
				 .mapParentId("parentCode")
				 .mapChecked(src->{
					 return rolePerms.contains(src.getCode()) && Integer.valueOf(0).equals(src.getChildrenSize());
				 })
				 .mapIsStateOpen(src->true)
				 .build(perms, null);
		
		return responseData(Arrays.asList(treeModel));
	}
	

	@ByPermissionClass(AssignPermission.class)
	@RequestMapping(value="{roleId}", method=RequestMethod.POST)
	public ModelAndView create(@PathVariable("roleId") long roleId, String[] permissionCodes){
		String msg = "保存权限成功！";
		this.adminRoleServiceImpl.saveRolePermission(roleId, permissionCodes);
		return messageMv(msg);
	}
	
	
}