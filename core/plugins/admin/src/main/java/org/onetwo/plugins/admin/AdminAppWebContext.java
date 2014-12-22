package org.onetwo.plugins.admin;

import org.onetwo.plugins.admin.controller.app.AppUserController;
import org.onetwo.plugins.admin.model.user.service.impl.AdminUserServiceImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses={AdminUserServiceImpl.class, AppUserController.class})
public class AdminAppWebContext {

}
