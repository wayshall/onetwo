package org.onetwo.plugins.admin.controller.app;


import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.ExtQuery.K.IfNull;
import org.onetwo.common.exception.BusinessException;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.admin.AdminModule.AppUser;
import org.onetwo.plugins.admin.controller.AdminBaseController;
import org.onetwo.plugins.admin.model.entity.AdminAppEntity;
import org.onetwo.plugins.admin.model.entity.AdminUserEntity;
import org.onetwo.plugins.admin.model.entity.AdminUserEntity.UserGender;
import org.onetwo.plugins.admin.model.entity.AdminUserEntity.UserStatus;
import org.onetwo.plugins.admin.model.service.impl.AdminAppServiceImpl;
import org.onetwo.plugins.admin.model.service.impl.AdminUserServiceImpl;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.Password;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenEdit;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenNew;
import org.onetwo.plugins.permission.anno.ByFunctionClass;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/app/user")
@Controller("adminAppUserController")
public class AppUserController extends AdminBaseController {
	 
	@Resource
	private AdminUserServiceImpl adminUserServiceImpl;
	
	@Resource
	private AdminAppServiceImpl adminAppServiceImpl;
	
	
//	@Resource
//	private AreaCodeServiceImpl areaCodeServiceImpl;

	
	@ByMenuClass(codeClass=AppUser.List.class)
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index(Page<AdminUserEntity> page, @ModelAttribute("user") AdminUserEntity user){
		List<AdminAppEntity> apps = this.adminAppServiceImpl.loadAllApps();
		String appCode = StringUtils.firstNotBlank(user.getAppCode(), apps.get(0).getCode());
		user.setAppCode(appCode);
//		userServiceImpl.findPage(page, "status:!=", UserStatus.DELETE);
		adminUserServiceImpl.findPage(page, "status:!=", UserStatus.DELETE,
										K.IF_NULL, IfNull.Ignore,
										"appCode", user.getAppCode(),
										"userName:like", user.getUserName(),
										"status", user.getStatus());
		ModelAndView mv = pluginMv("/admin/app/user-index", "page", page, "apps", apps);
		return appendDataForEdit(mv);
	}

	@ByFunctionClass(codeClass=AppUser.New.class)
	@RequestMapping(value="new", method=RequestMethod.GET)
	public ModelAndView _new(@ModelAttribute("user") AdminUserEntity user){
		ModelAndView mv = pluginMv("/admin/app/user-new");
		return appendDataForEdit(mv);
	}
	
	private ModelAndView appendDataForEdit(ModelAndView mv){
//		appendAreaList(mv);
		mv.addObject("genders", UserGender.values());
		mv.addObject("userStatus", new UserStatus[]{UserStatus.NORMAL, UserStatus.STOP});
		return mv;
	}

	@ByFunctionClass(codeClass=AppUser.New.class)
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(@ModelAttribute("user")AdminUserEntity user, BindingResult bind, RedirectAttributes redirectAttributes) throws BusinessException{
		this.validate(user, bind, ValidWhenNew.class, Password.class);
		if(bind.hasErrors()){
			return appendDataForEdit(pluginMv("/admin/app/user-new"));
		}
		/*if(!hasAreaPermission()){
			AreaCodeEntity area = new AreaCodeEntity();
			area.setAreaCode(this.getCurrentLoginUser().getAreaCode());
			user.setArea(area);
		}*/
		this.adminAppServiceImpl.saveAppUser(user);
//		addFlashMessage(redirectAttributes, "保存成功！");
		return pluginRedirectTo("/app/user?appCode="+user.getAppCode(), "保存成功！");
	}

	@ByFunctionClass(codeClass=AppUser.Edit.class)
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		AdminUserEntity user = this.adminUserServiceImpl.findUserWithRoles(id);
		user.setPassword("");

		ModelAndView mv = pluginMv("/admin/app/user-edit", "user", user, "userStatus", UserStatus.values());
//		List<MerchantInfoEntity> merchantList = infoServiceImpl.findByProperties("status", InfoStatus.ABLE.getValue(), "area.areaCode", user.getArea().getAreaCode());
//		List<MerchantInfoEntity> merchantList = infoServiceImpl.findByProperties("area.areaCode", user.getArea().getAreaCode());
		return appendDataForEdit(mv);
	}
	

	@ByFunctionClass(codeClass=AppUser.Edit.class)
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ModelAndView update(@ModelAttribute("user")AdminUserEntity user, BindingResult binding, RedirectAttributes redirectAttributes){
		this.validate(user, binding, ValidWhenEdit.class);
		if(user.getConfirmPassword().length()>0){
			this.validate(user, binding, Password.class);
		}
		if(binding.hasErrors()){
			return appendDataForEdit(pluginMv("/admin/app/user-edit"));
		}
		this.adminAppServiceImpl.saveAppUser(user);
//		addFlashMessage(redirectAttributes, "保存成功！");
		return pluginRedirectTo("/app/user/"+user.getId()+"/edit", "保存成功！");
	}
	

	@ByFunctionClass(codeClass=AppUser.Delete.class)
	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView deleteBatch(String appCode, @RequestParam(value="ids")long[] ids, RedirectAttributes redirectAttributes){
		this.adminUserServiceImpl.removeUsers(ids);
		return pluginRedirectTo("/app/user?appCode="+appCode, "删除成功！");
	}

	@ByFunctionClass(codeClass=AppUser.class)
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView show(@PathVariable("id") long id) throws BusinessException{
		AdminUserEntity user =  this.adminUserServiceImpl.findById(id);
		return pluginMv("/admin/app/user-show", "user", user);
	}
	
	
}