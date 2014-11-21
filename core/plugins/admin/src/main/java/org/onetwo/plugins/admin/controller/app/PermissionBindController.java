package org.onetwo.plugins.admin.controller.app;


import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.spring.web.mvc.DataResult;
import org.onetwo.common.utils.RoleIdDetail;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.plugins.admin.AdminModule.AppRole.AssignPermission;
import org.onetwo.plugins.admin.controller.AdminBaseController;
import org.onetwo.plugins.admin.model.entity.AdminPermissionEntity;
import org.onetwo.plugins.admin.model.entity.AdminRoleEntity;
import org.onetwo.plugins.admin.model.service.impl.AdminAppServiceImpl;
import org.onetwo.plugins.admin.model.service.impl.AdminRoleServiceImpl;
import org.onetwo.plugins.admin.model.vo.ZTreeMenuModel;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/app/permissionBind")
@Controller
public class PermissionBindController extends AdminBaseController {
	@Resource
	private AdminRoleServiceImpl adminRoleServiceImpl;
	
	@Resource
	private AdminAppServiceImpl adminAppServiceImpl;
	

	/*@ByMenuClass(codeClass=AssignPermission.class)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<RoleEntity> page){
		roleServiceImpl.findPage(page, "status:!=", RoleStatus.DELETE,
								K.IF_NULL, IfNull.Ignore,
								K.DATA_FILTER, false);
		
		List<AppEntity> apps = this.appServiceImpl.loadAllApps();
		ModelAndView mv = pluginMv("/admin/app/permission-bind-index", "page", page, "apps", apps);
		return mv;
	}*/

	@ByFunctionClass(codeClass=AssignPermission.class)
	@RequestMapping(value="{appCode}/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("appCode") String appCode, @PathVariable("id") long id){
		AdminRoleEntity role = adminRoleServiceImpl.findById(id);
		if(role.isSystemRoot()){
			return messageMv("系统管理无需分配权限");
		}
		final List<AdminPermissionEntity> rolePermissions = adminAppServiceImpl.findRoleAppPermissions(appCode, role.getId());
		final List<String> permIds = JFishList.wrap(rolePermissions).getPropertyList("code");
		
		List<ZTreeMenuModel> trees = null;
		if(this.getCurrentLoginUser().isSystemRootUser()){
			trees = adminAppServiceImpl.findAllAsTree(appCode, permIds);
		}else{
			UserDetail user = this.getCurrentLoginUser();
			if(RoleIdDetail.class.isInstance(user)){
				List<Long> roleIds = ((RoleIdDetail) user).getRoleIds();
				trees = adminAppServiceImpl.findAllAsTree(appCode, roleIds, permIds);
			}else{
				return messageMv("无法获取当前用户角色和权限！");
			}
		}
		
		String json = JsonMapper.DEFAULT_MAPPER.toJson(trees);
		
		return pluginMv("/admin/app/permission-bind-show", "menuJsonData", json);
	}
	

	@ByFunctionClass(codeClass=AssignPermission.class)
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public Object create(String appCode, long roleId, String[] permissionId){
		String msg = "保存权限成功！";
		Assert.hasText(appCode);
		try {
			this.adminAppServiceImpl.saveRolePermissions(appCode, roleId, permissionId);
		} catch (Exception e) {
			msg = e.getMessage();
			e.printStackTrace();
		}
		return DataResult.createSucceed(msg);
	}
	
	
}