package org.onetwo.plugins.admin.controller;

import org.onetwo.common.utils.Page;
import org.onetwo.easyui.EasyPage;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.plugins.admin.AdminModule.AdminUserMgr;
import org.onetwo.plugins.admin.entity.AdminUser;
import org.onetwo.plugins.admin.service.AdminUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("user")
public class AdminUserController extends WebAdminBaseController {

    @Autowired
    private AdminUserServiceImpl adminUserServiceImpl;
    
    
    @ByPermissionClass(AdminUserMgr.List.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(EasyPage<AdminUser> easyPage, AdminUser adminUser){
        return responsePageOrData("/admin/admin-user-index", ()->{
        			Page<AdminUser> page = Page.create(easyPage.getPage(), easyPage.getPageSize());
        			adminUserServiceImpl.findPage(page, adminUser);
                    return EasyPage.create(page);
                });
    }
    
    @ByPermissionClass(AdminUserMgr.Create.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(AdminUser adminUser){
        adminUserServiceImpl.save(adminUser);
        return messageMv("保存成功！");
    }
    
    @ByPermissionClass(AdminUserMgr.List.class)
    @RequestMapping(value="{id}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("id") Long id){
        AdminUser adminUser = adminUserServiceImpl.loadById(id);
        adminUser.setPassword("");
        return responseData(adminUser);
    }
    
    @ByPermissionClass(AdminUserMgr.Update.class)
    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Long id, AdminUser adminUser){
        adminUser.setId(id);
        adminUserServiceImpl.update(adminUser);
        return messageMv("更新成功！");
    }
    
    
    @ByPermissionClass(AdminUserMgr.Delete.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(Long[] ids){
        adminUserServiceImpl.deleteByIds(ids);
        return messageMv("删除成功！");
    }
}