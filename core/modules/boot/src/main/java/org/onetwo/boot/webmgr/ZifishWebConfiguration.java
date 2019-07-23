package org.onetwo.boot.webmgr;

import java.util.ArrayList;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.service.impl.SettingsManager;
import org.onetwo.boot.module.swagger.ScanPluginAsGroupSwaggerConfig;
import org.onetwo.boot.plugin.core.EnabledByPluginNameProperty;
import org.onetwo.boot.plugin.core.JFishWebPlugin;
import org.onetwo.common.file.FileStorer;
import org.onetwo.dbm.core.spi.DbmSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@JFishWebPlugin(ZifishWebPlugin.class)
//@ComponentScan(basePackageClasses=ZifishWebPlugin.class)
@ConditionalOnBean(BootSiteConfig.class)
// disable on zuul
@ConditionalOnMissingBean(name="zuulProxyMarkerBean")
public class ZifishWebConfiguration {

	@Bean
	public ChangelogController changelogController() {
		return new ChangelogController();
	}

	@Bean
	@ConditionalOnBean(WebManagementDelegater.class)
	public WebManagementController webManagementController() {
		return new WebManagementController();
	}
	/***
	 * 动态修改logger level
	 * @author wayshall
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(value=BootJFishConfig.ENABLE_DYNAMIC_LOGGER_LEVEL, matchIfMissing=false)
	public LoggerController loggerController(){
		return new LoggerController();
	}

	/***
	 * 动态修改默认配置
	 * @author wayshall
	 * @return
	 */
	@Configuration
	@ConditionalOnProperty(value=BootJFishConfig.ENABLE_DYNAMIC_SETTING, matchIfMissing=true)
	protected static class SettingsConfiguration {

		@Bean
		public SettingsController settingsController(){
			return new SettingsController();
		}

		@Bean
		@ConditionalOnMissingBean({SettingsManager.class})
		public SettingsManager settingsManager(){
			return new SettingsManager();
		}
	}
	
	@Bean
	@ConditionalOnMissingBean(UploadViewController.class)
	@ConditionalOnBean(FileStorer.class)
	@ConditionalOnProperty(BootSiteConfig.ENABLE_UPLOAD_VIEW)
	public UploadViewController uploadViewController(){
		return new UploadViewController();
	}
	
	@ConditionalOnClass(value=DbmSession.class)
	@Configuration
	static protected class DbmStatisControllerConfiguration {
		
		@Bean
		public DbmStatisController dbmStatisController(){
			return new DbmStatisController();
		}

	}
	
	@Configuration
	@EnableSwagger2
	@EnabledByPluginNameProperty(property="jfish.swagger", pluginClass=ZifishWebPlugin.class)
	static public class SwaggerConfig extends ScanPluginAsGroupSwaggerConfig {

		protected int registerDockets() {
			if (getServiceRootClass()==null) {
				return 0;
			}
			this.registerDocketsByWebApiAnnotation(0, getServiceName(), getServiceRootClass());
			return 1;
		}

	    @SuppressWarnings("rawtypes")
	    protected ApiInfo apiInfo(String applicationName) {
	        String serviceName = applicationName;
	        ApiInfo apiInfo = new ApiInfo(
	                serviceName,
	                serviceName + " REST API",
	                "1.0",
	                "termsOfServiceUrl",
	                new Contact("way", "", "weishao.zeng@gmail.com"),
	                "API License",
	                "API License URL",
	                new ArrayList<VendorExtension>());
	        return apiInfo;
	    }

	    protected String getServiceName() {
	    	return getServiceRootClass().getSimpleName();
	    }
	    /****
	     * 返回主项目的main class，用于扫描注册swagger
	     * 非主项目不需要覆盖实现
	     * @author weishao zeng
	     * @return
	     */
		protected Class<?> getServiceRootClass() {
			return ZifishWebPlugin.class;
		}
	}

}
