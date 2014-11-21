package org.onetwo.plugins.admin.controller.app;


import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.fish.plugin.PluginSupportedController;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.admin.AdminModule.AppRole;
import org.onetwo.plugins.admin.model.entity.AdminAppEntity;
import org.onetwo.plugins.admin.model.entity.AdminRoleEntity;
import org.onetwo.plugins.admin.model.entity.AdminRoleEntity.RoleCode;
import org.onetwo.plugins.admin.model.entity.AdminRoleEntity.RoleStatus;
import org.onetwo.plugins.admin.model.service.impl.AdminAppServiceImpl;
import org.onetwo.plugins.admin.model.service.impl.AdminRoleServiceImpl;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/app/role")
@Controller("adminAppRoleController")
public class AppRoleController extends PluginSupportedController {
	 
	@Autowired
	private AdminRoleServiceImpl adminRoleServiceImpl;

	@Resource
	private AdminAppServiceImpl adminAppServiceImpl;

	@ByMenuClass(codeClass=AppRole.List.class)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<AdminRoleEntity> page, @ModelAttribute("role") AdminRoleEntity role){
		List<AdminAppEntity> apps = this.adminAppServiceImpl.loadAllApps();
		String appCode = StringUtils.firstNotBlank(role.getAppCode(), apps.get(0).getCode());
		role.setAppCode(appCode);
		
		adminRoleServiceImpl.findPage(page, "status", RoleStatus.NORMAL, 
								"appCode", role.getAppCode());
		
		return pluginMv("/admin/app/role-index", "page", page, "apps", apps);
	}

	private ModelAndView appendDataForInput(ModelAndView mv){
//		appendAreaList(mv);
		mv.addObject("roleStatusList", RoleStatus.values());
		mv.addObject("roleCodes", RoleCode.values());
		return mv;
	}
	
	@ByFunctionClass(codeClass=AppRole.New.class)
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("role") AdminRoleEntity role){
		ModelAndView mv = pluginMv("/admin/app/role-new");
		return appendDataForInput(mv);
	}

	@ByFunctionClass(codeClass=AppRole.New.class)
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(@Valid @ModelAttribute("role")AdminRoleEntity role, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		if(bind.hasErrors()){
			return _new(role);
		}
		/*if(!hasAreaPermission()){
			role.setArea(new AreaCodeEntity(getCurrentLoginUser().getAreaCode()));
		}*/
		this.adminAppServiceImpl.saveAppRole(role);
		return pluginRedirectTo("/app/role?appCode="+role.getAppCode(), "保存成功！");
	}

	@ByFunctionClass(codeClass=AppRole.Edit.class)
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		AdminRoleEntity role = this.adminRoleServiceImpl.findById(id);
		
		ModelAndView mv = pluginMv("/admin/app/role-edit", "role", role);
		return appendDataForInput(mv);
	}
	
	@ByFunctionClass(codeClass=AppRole.Edit.class)
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("role") @Valid AdminRoleEntity role, BindingResult binding, RedirectAttributes redirectAttributes){
		if(binding.hasErrors()){
			ModelAndView mv = pluginMv("/admin/app/role-edit", "role", role);
			return appendDataForInput(mv);
		}
		/*if(!hasAreaPermission()){
			role.setArea(new AreaCodeEntity(getCurrentLoginUser().getAreaCode()));
		}*/
		this.adminAppServiceImpl.saveAppRole(role);
		addFlashMessage(redirectAttributes, "保存成功！");
		return redirectTo("/admin/app/role?appCode="+role.getAppCode());
	}
	

	@ByFunctionClass(codeClass=AppRole.Delete.class)
	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(String appCode, @RequestParam(value="ids")long[] ids, RedirectAttributes redirectAttributes){
		this.adminRoleServiceImpl.removeRoles(ids);
		return pluginRedirectTo("/app/role?appCode="+appCode, "删除成功！");
	}

	@ByFunctionClass(codeClass=AppRole.class)
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id){
		AdminRoleEntity role =  this.adminRoleServiceImpl.findById(id);
		return pluginMv("/admin/app/role-show", "role", role);
	}
	
	
}