

package org.onetwo.plugins.admin.controller;

import java.util.List;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.controller.DateInitBinder;
import org.onetwo.common.spring.validator.ValidatorUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.easyui.EasyDataGrid;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.plugins.admin.AdminModule.ApplicationMgr;
import org.onetwo.plugins.admin.AdminModule.RoleMgr;
import org.onetwo.plugins.admin.entity.AdminApplication;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidAnyTime;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenEdit;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenNew;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/application")
public class ApplicationController extends AbstractBaseController implements DateInitBinder {

    @ByPermissionClass(ApplicationMgr.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(Page<AdminApplication> page, AdminApplication application){
        return responsePageOrData("/application-index", ()->{
        		AdminApplication.MANAGER.findPageByExample(page, application);
                    return EasyDataGrid.create(page);
                });
    }
    
    @ByPermissionClass(ApplicationMgr.Create.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(@Validated({ValidAnyTime.class, ValidWhenNew.class}) AdminApplication application, BindingResult br){
    	ValidatorUtils.throwIfHasErrors(br, true);
    	AdminApplication.MANAGER.save(application);
        return messageMv("保存成功！");
    }
    @ByPermissionClass(ApplicationMgr.class)
    @RequestMapping(value="{code}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("code") String code){
    	AdminApplication application = AdminApplication.MANAGER.findById(code);
        return responseData(application);
    }
    
    @ByPermissionClass(ApplicationMgr.Update.class)
    @RequestMapping(value="{code}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("code") String code, @Validated({ValidAnyTime.class, ValidWhenEdit.class}) AdminApplication application, BindingResult br){
    	ValidatorUtils.throwIfHasErrors(br, true);
        application.setCode(code);
        AdminApplication.MANAGER.update(application);
        return messageMv("更新成功！");
    }
    
    
    @ByPermissionClass(ApplicationMgr.Delete.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(String[] codes){
    	AdminApplication.MANAGER.removeByIds(codes);
        return messageMv("删除成功！");
    }
    

    @ByPermissionClass(RoleMgr.AssignPermission.class)
    @RequestMapping(value="bindPermission", method=RequestMethod.GET)
    public ModelAndView bindPermission(Long roleId){
    	List<AdminApplication> apps = AdminApplication.MANAGER.findAll();
        return mv("/admin/application-index", "apps", apps, "roleId", roleId);
    }
}