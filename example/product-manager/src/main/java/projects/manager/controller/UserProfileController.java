package projects.manager.controller;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.common.spring.validator.ValidatorUtils;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.onetwo.ext.security.utils.LoginUserDetails;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidAnyTime;
import org.onetwo.plugins.admin.utils.WebConstant.ValidGroup.ValidWhenEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import projects.manager.entity.User;
import projects.manager.service.impl.UserServiceImpl;
import projects.manager.utils.Apps.SystemMgr.UserProfile;

@Controller
@RequestMapping("/manager/userProfile")
public class UserProfileController extends AbstractBaseController {

    @Autowired
    private UserServiceImpl adminUserServiceImpl;
    

    @ByPermissionClass(UserProfile.class)
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView showProfile(LoginUserDetails loginUser){
        return responsePageOrData("/manager/user-profile", ()->{
        	User adminUser = adminUserServiceImpl.loadById(loginUser.getUserId());
            adminUser.setPassword("");
            return adminUser;
        });
    }
    
    @ByPermissionClass(UserProfile.class)
    @RequestMapping(method=RequestMethod.PUT)
    public ModelAndView update(@Validated({ValidAnyTime.class, ValidWhenEdit.class}) User adminUser, BindingResult br){
    	ValidatorUtils.throwIfHasErrors(br, true);
    	LoginUserDetails loginUser = checkAndGetCurrentLoginUser(LoginUserDetails.class, true);
        adminUser.setId(loginUser.getUserId());
        adminUser.setUserName(loginUser.getUserName());
        adminUserServiceImpl.update(adminUser);
        return messageMv("更新成功！");
    }
    
}