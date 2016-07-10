package org.onetwo.plugins.admin.controller;

import java.util.List;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.common.utils.map.MappableMap;
import org.onetwo.easyui.EasyModel;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.plugins.admin.AdminModule.AdminUserMgr.AssignRole;
import org.onetwo.plugins.admin.entity.AdminRole;
import org.onetwo.plugins.admin.entity.AdminUser;
import org.onetwo.plugins.admin.service.AdminRoleServiceImpl;
import org.onetwo.plugins.admin.service.AdminUserServiceImpl;
import org.onetwo.plugins.admin.utils.Enums.CommonStatus;
import org.onetwo.plugins.admin.utils.Enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/admin/userRole")
@Controller
public class UserRoleController extends AbstractBaseController {
	@Autowired
	private AdminUserServiceImpl adminUserServiceImpl;
	@Autowired
	private AdminRoleServiceImpl adminRoleServiceImpl;

	@ByPermissionClass(AssignRole.class)
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("userId") long userId) {
		AdminUser user = adminUserServiceImpl.loadById(userId);
		if(UserStatus.of(user.getStatus())!=UserStatus.NORMAL)
			return messageMv("用户非正常状态，不能分配角色！");
		
		List<AdminRole> roles = adminRoleServiceImpl.findByStatus(CommonStatus.NORMAL, null);
		List<Long> roleIds = adminRoleServiceImpl.findRoleIdsByUser(userId);
		
		List<MappableMap> rolelist = EasyModel.newComboBoxBuilder(AdminRole.class)
				 .specifyMappedFields()
				 .mapText("name")
				 .mapValue("id")
				 .mapSelected(role->roleIds.contains(role.getId()))
				 .build(roles);
		
		return responseData(rolelist);
	}

	@ByPermissionClass(AssignRole.class)
	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	public ModelAndView create(Long[] roleIds, @PathVariable("userId") long userId, RedirectAttributes redirectAttributes) {
		this.adminRoleServiceImpl.saveUserRoles(userId, roleIds);
		return messageMv("分配角色成功！");
	}

}