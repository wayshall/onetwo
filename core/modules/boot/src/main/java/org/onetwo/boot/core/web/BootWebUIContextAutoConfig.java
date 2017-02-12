package org.onetwo.boot.core.web;

import javax.servlet.Filter;

import org.onetwo.boot.core.BootContextConfig;
import org.onetwo.boot.core.BootWebCommonAutoConfig;
import org.onetwo.boot.core.config.BootBusinessConfig;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.StoreType;
import org.onetwo.boot.core.config.BootSiteConfig.UploadConfig;
import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.filter.CorsFilter;
import org.onetwo.boot.core.web.service.BootCommonService;
import org.onetwo.boot.core.web.service.impl.SimpleBootCommonService;
import org.onetwo.boot.plugin.PluginContextConfig;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.SimpleFileStorer;
import org.onetwo.common.ftp.FtpClientManager.FtpConfig;
import org.onetwo.common.ftp.FtpFileStorer;
import org.onetwo.common.web.init.CommonWebFilterInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.HttpEncodingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

@Configuration
//@EnableConfigurationProperties({JFishBootConfig.class, SpringBootConfig.class})
@EnableConfigurationProperties({HttpEncodingProperties.class, BootJFishConfig.class, BootSpringConfig.class, BootBusinessConfig.class, BootSiteConfig.class})
@Import({BootContextConfig.class, PluginContextConfig.class})
//@Import({BootContextConfig.class, PluginContextConfig.class, FixGroovyWebConfiguration.class})
//@Import({BootContextConfig.class})
@ConditionalOnProperty(name=BootJFishConfig.ENABLE_JFISH_AUTO_CONFIG, havingValue=BootJFishConfig.VALUE_AUTO_CONFIG_WEB_UI, matchIfMissing=true)
@ConditionalOnClass(CommonWebFilterInitializer.class)
public class BootWebUIContextAutoConfig extends BootWebCommonAutoConfig {
//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private HttpEncodingProperties httpEncodingProperties;
	
	public BootWebUIContextAutoConfig(){
	}
	/*@Bean
	public BootSiteConfig bootSiteConfig(){
		return bootSiteConfig;
	}*/
	
	/****
	 * CorsFilter 须在所有filter之前，包括security的filter
	 * 否则会抛 No 'Access-Control-Allow-Origin' header is present on the requested resource
	 * filter
	 * @return
	 */
	@Bean
	@ConditionalOnBean(name = CorsFilter.CORS_FILTER_NAME)
	public FilterRegistrationBean corsFilterRegistration(@Qualifier(CorsFilter.CORS_FILTER_NAME) Filter filter){
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
		registration.setName(CorsFilter.CORS_FILTER_NAME);
		return registration;
	}
	
	@Bean
	@ConditionalOnMissingBean(FileStorer.class)
	@ConditionalOnBean(AbstractBaseController.class)
	public FileStorer<?> fileStorer(){
		UploadConfig config = bootSiteConfig.getUpload();
		StoreType type = config.getStoreType();
		
		if(type==StoreType.LOCAL){
			SimpleFileStorer fs = new SimpleFileStorer();
			fs.setStoreBaseDir(config.getFileStorePath());
			fs.setAppContextDir(config.getAppContextDir());
			return fs;
		}else if(type==StoreType.FTP){
			FtpConfig ftpConfig = new FtpConfig();
			ftpConfig.setEncoding(config.getFtpEncoding());
			ftpConfig.setServer(config.getFtpServer());
			ftpConfig.setPort(config.getFtpPort());
			
			FtpFileStorer fs = new FtpFileStorer(ftpConfig);
			fs.setLoginParam(config.getFtpUser(), config.getFtpPassword());
//			fs.setStoreBaseDir(config.getFtpBaseDir());
			fs.setStoreBaseDir(config.getFileStorePath());
			fs.setAppContextDir(config.getAppContextDir());
			return fs;
		}
		throw new IllegalArgumentException("type: " + type);
	}
	
	@Bean
	@ConditionalOnMissingBean(BootCommonService.class)
	@ConditionalOnProperty(BootSiteConfig.ENABLE_UPLOAD_PREFIX)
	public BootCommonService bootCommonService(){
		SimpleBootCommonService service = new SimpleBootCommonService();
		return service;
	}
	
	
	/***
	 * instead of boot mapper config by JacksonAutoConfiguration
	 * @return
	 */
	/*@Bean
	public ObjectMapper objectMapper(){
		return BootWebUtils.createObjectMapper(applicationContext);
	}*/
	
	/*@Bean
	public WebHolderManager webHolderManager() {
		WebHolderManager webHolderManager = new WebHolderManager();
		return webHolderManager;
	}*/
	

}
