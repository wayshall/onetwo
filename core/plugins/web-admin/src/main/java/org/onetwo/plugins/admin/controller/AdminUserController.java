package org.onetwo.plugins.admin.controller;

import org.onetwo.boot.core.web.view.XResponseView;
import org.onetwo.common.utils.Page;
import org.onetwo.easyui.EasyViews.EasyGridView;
import org.onetwo.easyui.PageRequest;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.plugins.admin.AdminModule.UserMgr;
import org.onetwo.plugins.admin.entity.AdminUser;
import org.onetwo.plugins.admin.service.impl.AdminUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("user")
public class AdminUserController extends WebAdminBaseController {

    @Autowired
    private AdminUserServiceImpl adminUserServiceImpl;
    

    @ByPermissionClass(UserMgr.class)
    @RequestMapping(method=RequestMethod.GET)
    @XResponseView(value="easyui", wrapper=EasyGridView.class)
    public ModelAndView index(PageRequest easyPage, AdminUser adminUser){
        return responsePageOrData("/admin-user-index", ()->{
        			Page<AdminUser> page = easyPage.toPageObject();//Page.create(easyPage.getPage(), easyPage.getPageSize());
        			adminUserServiceImpl.findPage(page, adminUser);
        			return page;
                });
    }
    @ByPermissionClass(value=UserMgr.class, overrideMenuUrl=false)
    @RequestMapping(value="export", method=RequestMethod.GET)
    public ModelAndView export(PageRequest easyPage, AdminUser adminUser){
    	Page<AdminUser> page = easyPage.toPageObject();//Page.create(easyPage.getPage(), easyPage.getPageSize());
    	page.noLimited();
		adminUserServiceImpl.findPage(page, adminUser);
        return pluginMv("admin-user-export", "datas", page.getResult());
    }
    
    @ByPermissionClass(UserMgr.Create.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(AdminUser adminUser, MultipartFile avatarFile){
        adminUserServiceImpl.save(adminUser, avatarFile);
        return messageMv("保存成功！");
    }
    
    @ByPermissionClass(UserMgr.class)
    @RequestMapping(value="{id}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("id") Long id){
        AdminUser adminUser = adminUserServiceImpl.loadById(id);
        adminUser.setPassword("");
        return responseData(adminUser);
    }
    
    @ByPermissionClass(UserMgr.Update.class)
    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Long id, AdminUser adminUser){
        adminUser.setId(id);
        adminUserServiceImpl.update(adminUser);
        return messageMv("更新成功！");
    }
    
    
    @ByPermissionClass(UserMgr.Delete.class)
    @RequestMapping(method=RequestMethod.DELETE)
    public ModelAndView deleteBatch(Long[] ids){
        adminUserServiceImpl.deleteByIds(ids);
        return messageMv("删除成功！");
    }
}