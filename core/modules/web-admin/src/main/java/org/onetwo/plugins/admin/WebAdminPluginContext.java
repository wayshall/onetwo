package org.onetwo.plugins.admin;

import javax.annotation.PostConstruct;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.ext.permission.service.MenuItemRepository;
import org.onetwo.ext.permission.service.impl.DefaultMenuItemRepository;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.plugins.admin.controller.LoginController;
import org.onetwo.plugins.admin.entity.AdminUser;
import org.onetwo.plugins.admin.service.impl.AdminUserDetailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@ComponentScan(basePackageClasses={WebAdminPluginContext.class})
public class WebAdminPluginContext {
	
	final private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SecurityConfig securityConfig;
	@Autowired
	private BootSiteConfig bootSiteConfig;
	
	@PostConstruct
	public void init(){
		String targetUrl = bootSiteConfig.getBaseURL()+"/web-admin/index";
		logger.info("targetUrl: "+targetUrl);
		this.securityConfig.setAfterLoginUrl(targetUrl);
	}
	
	@Bean
	@ConditionalOnMissingBean(UserDetailsService.class)
	public UserDetailsService userDetailsService(){
		return new AdminUserDetailServiceImpl<AdminUser>(AdminUser.class);
	}
	
	@Bean
	@ConditionalOnMissingBean(name="loginController")
	public LoginController loginController(){
		return new LoginController();
	}
	
	@Bean
	public WebAdminPlugin webAdminPlugin(){
		return new WebAdminPlugin();
	}
	

	@Bean
//	@ConditionalOnBean(AdminController.class)
	@ConditionalOnMissingBean(MenuItemRepository.class)
	public MenuItemRepository<PermisstionTreeModel> menuItemRepository(){
		DefaultMenuItemRepository menuItemRepository = new DefaultMenuItemRepository();
		return menuItemRepository;
	}
	
	/*@Bean
	@ConditionalOnBean(DictionaryService.class)
	@ConditionalOnMissingBean(DictionaryImportController.class)
	public DictionaryImportController dictionaryImportController(){
		return new DictionaryImportController();
	}*/

}
