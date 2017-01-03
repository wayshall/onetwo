package projects.manager.controller;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.common.spring.validator.ValidatorUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.easyui.EasyDataGrid;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidAnyTime;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenEdit;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import projects.manager.entity.User;
import projects.manager.service.impl.UserServiceImpl;
import projects.manager.utils.Products.SystemMgr.UserMgr;
import projects.manager.vo.LoginUserInfo;

@Controller
@RequestMapping("/manager/user")
public class UserController extends AbstractBaseController {

    @Autowired
    private UserServiceImpl adminUserServiceImpl;
    
    
    @ByPermissionClass(UserMgr.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(EasyDataGrid<User> easyPage, User adminUser){
        return responsePageOrData("/manager/user-index", ()->{
        			Page<User> page = Page.create(easyPage.getPage(), easyPage.getPageSize());
        			adminUserServiceImpl.findPage(this.getCurrentLoginUser(LoginUserInfo.class), page, adminUser);
                    return EasyDataGrid.create(page);
                });
    }
    
    @ByPermissionClass(UserMgr.Create.class)
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView create(@Validated({ValidAnyTime.class, ValidWhenNew.class}) User adminUser, BindingResult br){
    	ValidatorUtils.throwIfHasErrors(br, true);
        adminUserServiceImpl.save(this.getCurrentLoginUser(LoginUserInfo.class), adminUser);
        return messageMv("保存成功！");
    }

    @ByPermissionClass(value=UserMgr.class)
    @RequestMapping(value="{id}", method=RequestMethod.GET)
    public ModelAndView show(@PathVariable("id") Long id){
        User adminUser = adminUserServiceImpl.loadById(id);
        adminUser.setPassword("");
        return responseData(adminUser);
    }
    
    @ByPermissionClass(UserMgr.Update.class)
    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public ModelAndView update(@PathVariable("id") Long id, @Validated({ValidAnyTime.class, ValidWhenEdit.class}) User adminUser, BindingResult br){
    	ValidatorUtils.throwIfHasErrors(br, true);
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