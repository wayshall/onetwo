package org.onetwo.plugins.admin.controller.app;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.plugins.admin.AdminModule.AppUser;
import org.onetwo.plugins.admin.controller.AdminBaseController;
import org.onetwo.plugins.admin.model.user.entity.AdminRoleEntity;
import org.onetwo.plugins.admin.model.user.entity.AdminUserEntity;
import org.onetwo.plugins.admin.model.user.entity.AdminUserEntity.UserStatus;
import org.onetwo.plugins.admin.model.user.service.impl.AdminAppServiceImpl;
import org.onetwo.plugins.admin.model.user.service.impl.AdminRoleServiceImpl;
import org.onetwo.plugins.admin.model.user.service.impl.AdminUserServiceImpl;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/app/user-role")
@Controller("adminAppUserRoleController")
public class AppUserRoleController extends AdminBaseController {
	@Autowired
	private AdminUserServiceImpl adminUserServiceImpl;
	@Autowired
	private AdminRoleServiceImpl adminRoleServiceImpl;
	
	@Resource
	private AdminAppServiceImpl adminAppServiceImpl;

	@ByFunctionClass(codeClass = AppUser.AssignRole.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id) {
		AdminUserEntity user = adminUserServiceImpl.findUserWithRoles(id);
		if(user.getStatus()!=UserStatus.NORMAL)
			return messageMv("用户非正常状态，不能分配角色！");
		List<AdminRoleEntity> roles = adminAppServiceImpl.findAppRoles(user.getAppCode());
		return pluginMv("/admin/app/user-role-show", "user", user, "roles", roles);
	}

	@ByFunctionClass(codeClass = AppUser.AssignRole.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Object create(long[] roleIds, @PathVariable("id") long id,
			RedirectAttributes redirectAttributes) {
		AdminUserEntity user = this.adminAppServiceImpl.saveUserRoles(id, roleIds);
		return pluginRedirectTo("/app/user?appCode="+user.getAppCode(), "分配角色成功！");
	}

}