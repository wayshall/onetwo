package org.onetwo.plugins.permission;

import org.onetwo.plugins.permission.web.MenuController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=MenuController.class)
public class PermissionMvcContext {
	
}
