package org.onetwo.plugins.admin;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.module.dbm.DbmContextAutoConfig;
import org.onetwo.boot.module.security.oauth2.NotEnableOauth2SsoCondition;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.dquery.annotation.DbmPackages;
import org.onetwo.common.spring.Springs;
import org.onetwo.ext.permission.api.PermissionConfig;
import org.onetwo.ext.permission.entity.PermisstionTreeModel;
import org.onetwo.ext.permission.parser.DefaultMenuInfoParser;
import org.onetwo.ext.permission.parser.MenuInfoParser;
import org.onetwo.ext.permission.service.MenuItemRepository;
import org.onetwo.ext.permission.service.impl.DefaultMenuItemRepository;
import org.onetwo.ext.security.utils.SecurityConfig;
import org.onetwo.plugins.admin.controller.LoginController;
import org.onetwo.plugins.admin.controller.WebAdminBaseController;
import org.onetwo.plugins.admin.entity.AdminPermission;
import org.onetwo.plugins.admin.entity.AdminUser;
import org.onetwo.plugins.admin.service.DictionaryImportService;
import org.onetwo.plugins.admin.service.impl.AdminUserDetailServiceImpl;
import org.onetwo.plugins.admin.service.impl.PermissionManagerImpl;
import org.onetwo.plugins.admin.utils.WebAdminPermissionConfig;
import org.onetwo.plugins.admin.utils.WebAdminPermissionConfig.RootMenuClassProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@AutoConfigureAfter(DbmContextAutoConfig.class)
@ConditionalOnProperty(name="jfish.plugins.web-admin.enable", havingValue="true", matchIfMissing=true)
@DbmPackages
public class WebAdminPluginContext {
	
//	final private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required=false)
	private SecurityConfig securityConfig;
	@Autowired
	private BootSiteConfig bootSiteConfig;

	/****
	 * trigger baseEntityManager init and load the sql file
	 */
	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	public WebAdminPluginContext(){
	}
	
	@PostConstruct
	public void init(){
		if(securityConfig!=null){
			String targetUrl = bootSiteConfig.getBaseURL()+"/web-admin/index";
//			logger.info("targetUrl: "+targetUrl);
			this.securityConfig.setAfterLoginUrl(targetUrl);
		}
	}
	
	
	@Bean
	public WebAdminPlugin webAdminPlugin(){
		return new WebAdminPlugin();
	}
	

	
	/*@Bean
	@ConditionalOnBean(DictionaryService.class)
	@ConditionalOnMissingBean(DictionaryImportController.class)
	public DictionaryImportController dictionaryImportController(){
		return new DictionaryImportController();
	}*/

	/****
	 * 如果是sso client端，则不需要启用
	 * @author way
	 *
	 */
	@ComponentScan(basePackageClasses={WebAdminBaseController.class, DictionaryImportService.class, WebAdminPermissionConfig.class})
	@Configuration
	@Conditional(NotEnableOauth2SsoCondition.class)
	protected static class WebAdminManagerModule {
		
		public WebAdminManagerModule(){
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
//		@ConditionalOnBean(AdminController.class)
		@ConditionalOnMissingBean(MenuItemRepository.class)
		public MenuItemRepository<PermisstionTreeModel> menuItemRepository(){
			DefaultMenuItemRepository menuItemRepository = new DefaultMenuItemRepository();
			return menuItemRepository;
		}
	}
	
	
	@Configuration
	protected static class JavaClassStylePermissionManager {
		@Bean
		@Autowired
		@ConditionalOnBean(RootMenuClassProvider.class)
		public WebAdminPermissionConfig webAdminPermissionConfig(RootMenuClassProvider provider){
			WebAdminPermissionConfig config = new WebAdminPermissionConfig();
			config.setRootMenuClassProvider(provider);
			return config;
		}
		
		@Bean
		@Autowired
		public PermissionManagerImpl permissionManagerImpl(List<PermissionConfig<AdminPermission>> configs){
			PermissionManagerImpl manager = new PermissionManagerImpl();
			List<MenuInfoParser<AdminPermission>> parsers = configs.stream().map(cfg->{
				return new DefaultMenuInfoParser<AdminPermission>(cfg);
			})
			.collect(Collectors.toList());
			manager.setParsers(parsers);
			return manager;
		}
	}

}
