package org.onetwo.boot.core.web;

import java.util.List;

import javax.annotation.PostConstruct;

import org.onetwo.boot.core.BootContextConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.JFishBootConfig;
import org.onetwo.boot.core.config.JFishBootConfig.StoreType;
import org.onetwo.boot.core.config.JFishBootConfig.UploadConfig;
import org.onetwo.boot.core.init.BootServletContextInitializer;
import org.onetwo.boot.core.web.ftl.FreemarkerViewContextConfig;
import org.onetwo.boot.core.web.mvc.BootStandardServletMultipartResolver;
import org.onetwo.boot.core.web.mvc.BootWebExceptionResolver;
import org.onetwo.boot.core.web.mvc.RequestMappingHandlerMappingListenable;
import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.userdetails.BootSessionUserManager;
import org.onetwo.boot.core.web.view.BootJsonView;
import org.onetwo.common.fs.FileStorer;
import org.onetwo.common.fs.SimpleFileStorer;
import org.onetwo.common.ftp.FtpClientManager.FtpConfig;
import org.onetwo.common.ftp.FtpFileStorer;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.common.web.utils.WebHolderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

@Configuration
//@EnableConfigurationProperties({JFishBootConfig.class, SpringBootConfig.class})
@Import({BootContextConfig.class, FreemarkerViewContextConfig.class})
//@Import({BootContextConfig.class})
public class BootWebContextConfig {
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private JFishBootConfig jfishBootConfig;
	
	@PostConstruct
	public void init(){
		SpringApplication.initApplicationIfNotInitialized(applicationContext);
	}
	
	@Bean
	public BootSiteConfig bootSiteConfig(){
		return BootSiteConfig.getInstance();
	}
	
	@Bean
	@ConditionalOnMissingBean(FileStorer.class)
	public FileStorer<?> fileStorer(){
		UploadConfig config = jfishBootConfig.getUpload();
		StoreType type = config.getStoreType();
		
		if(type==StoreType.LOCAL){
			SimpleFileStorer fs = new SimpleFileStorer();
			fs.setStoreDir(config.getFileStorePath());
			return fs;
		}else if(type==StoreType.FTP){
			FtpConfig ftpConfig = new FtpConfig();
			ftpConfig.setEncoding(config.getFtpEncoding());
			ftpConfig.setServer(config.getFtpServer());
			ftpConfig.setPort(config.getFtpPort());
			
			FtpFileStorer fs = new FtpFileStorer(ftpConfig);
			fs.setLoginParam(config.getFtpUser(), config.getFtpPassword());
			fs.setStoreDir(config.getFtpBaseDir());
			return fs;
		}
		throw new IllegalArgumentException("type: " + type);
	}
	
	@Bean
	@ConditionalOnMissingBean(BootServletContextInitializer.class)
	public BootServletContextInitializer bootServletContextInitializer(){
		return new BootServletContextInitializer();
	}
	
	@Bean
	RequestMappingHandlerMappingListenable requestMappingHandlerMappingListenable(){
		RequestMappingHandlerMappingListenable req = new RequestMappingHandlerMappingListenable();
		return req;	
	}
	
	@Bean
	public BootMvcConfigurerAdapter bootMvcConfigurerAdapter(){
		return new BootMvcConfigurerAdapter();
	}
	
	/***
	 * 异常解释
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(BootWebExceptionResolver.class)
	public BootWebExceptionResolver bootWebExceptionResolver(){
		return new BootWebExceptionResolver();
	}
	
	/***
	 * 拦截器
	 * @return
	 */
	@Bean
	public BootFirstInterceptor bootFirstInterceptor(){
		return new BootFirstInterceptor();
	}
	
	@Bean
	public BootJsonView bootJsonView(){
		return new BootJsonView();
	}
	
	@Bean
	public WebHolderManager webHolderManager() {
		WebHolderManager webHolderManager = new WebHolderManager();
		return webHolderManager;
	}

	/***
	 * 协作视图
	 * @param applicationContext
	 * @param contentNegotiationManager
	 * @return
	 */
	@Bean
	@Autowired
	public ViewResolver viewResolver(ApplicationContext applicationContext, ContentNegotiationManager contentNegotiationManager) {
		List<View> views = SpringUtils.getBeans(applicationContext, View.class);
		ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
		viewResolver.setUseNotAcceptableStatusCode(true);
		viewResolver.setOrder(0);
		viewResolver.setDefaultViews(views);
//		List<View> views = LangUtils.asListWithType(View.class, xmlView(), jsonView());
//		viewResolver.setMediaTypes(mediaType());
//		viewResolver.setDefaultContentType(MediaType.TEXT_HTML);
//		viewResolver.setIgnoreAcceptHeader(true);
		viewResolver.setContentNegotiationManager(contentNegotiationManager);
		return viewResolver;
	}
	
	@Bean(name=MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME)
	public MultipartResolver filterMultipartResolver(){
		BootStandardServletMultipartResolver resolver = new BootStandardServletMultipartResolver();
		resolver.setMaxUploadSize(jfishBootConfig.getUpload().getMaxUploadSize());
		return resolver;
	}

	@Bean
	@ConditionalOnMissingBean(SessionUserManager.class)
	public SessionUserManager<UserDetail> sessionUserManager(){
		return new BootSessionUserManager();
	}
	/*@Bean(name=MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME)
	public CommonsMultipartResolver filterMultipartResolver(){
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		resolver.setMaxUploadSize(jfishBootConfig.getUpload().getMaxUploadSize());
		return resolver;
	}*/
	
//	@Bean
	/*private ContentNegotiationManagerFactoryBean contentNegotiationManagerFactoryBean(){
		ContentNegotiationManagerFactoryBean bean = new ContentNegotiationManagerFactoryBean();
		bean.setMediaTypes(jfishBootConfig.getMediaType());
		bean.setDefaultContentType(MediaType.TEXT_HTML);
		bean.setIgnoreAcceptHeader(true);
		bean.setFavorParameter(true);
		return bean;
	}*/
	
	/*@Bean
	public BootFirstInterceptor bootFirstInterceptor(){
		return new BootFirstInterceptor();
	}*/

}
