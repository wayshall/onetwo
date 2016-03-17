package org.onetwo.boot.core.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

import org.onetwo.boot.core.BootContextConfig;
import org.onetwo.boot.core.config.BootBusinessConfig;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.StoreType;
import org.onetwo.boot.core.config.BootSiteConfig.UploadConfig;
import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.boot.core.init.BootServletContextInitializer;
import org.onetwo.boot.core.web.filter.CorsFilter;
import org.onetwo.boot.core.web.ftl.FreemarkerViewContextConfig;
import org.onetwo.boot.core.web.mvc.BootStandardServletMultipartResolver;
import org.onetwo.boot.core.web.mvc.BootWebExceptionResolver;
import org.onetwo.boot.core.web.mvc.RequestMappingHandlerMappingListenable;
import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.mvc.interceptor.UploadValidateInterceptor;
import org.onetwo.boot.core.web.service.BootCommonService;
import org.onetwo.boot.core.web.service.impl.SimpleBootCommonService;
import org.onetwo.boot.core.web.userdetails.BootSessionUserManager;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.boot.core.web.view.BootJsonView;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.SimpleFileStorer;
import org.onetwo.common.ftp.FtpClientManager.FtpConfig;
import org.onetwo.common.ftp.FtpFileStorer;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;
import org.onetwo.common.web.utils.WebHolderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.HttpEncodingProperties;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
//@EnableConfigurationProperties({JFishBootConfig.class, SpringBootConfig.class})
@EnableConfigurationProperties({HttpEncodingProperties.class, BootJFishConfig.class, BootSpringConfig.class, BootBusinessConfig.class, BootSiteConfig.class})
@Import({BootContextConfig.class, FreemarkerViewContextConfig.class})
//@Import({BootContextConfig.class})
@ConditionalOnProperty(prefix="jfish", name="autoConfig", havingValue="true", matchIfMissing=true)
public class BootWebAContextAutoConfig {
//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private BootJFishConfig bootJfishConfig;
	
	@Autowired
	private BootSiteConfig bootSiteConfig;
	
	@Autowired
	private HttpEncodingProperties httpEncodingProperties;
	
	@PostConstruct
	public void init(){
		SpringApplication.initApplicationIfNotInitialized(applicationContext);
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
	public BootCommonService bootCommonService(){
		SimpleBootCommonService service = new SimpleBootCommonService();
		return service;
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
//	@Autowired
	public BootWebExceptionResolver bootWebExceptionResolver(){
		BootWebExceptionResolver resolver = new BootWebExceptionResolver();
//		resolver.setExceptionMessage(exceptionMessage);
		return resolver;
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
	public UploadValidateInterceptor uploadValidateInterceptor(){
		return new UploadValidateInterceptor();
	}
	
	@Bean
	public BootJsonView bootJsonView(){
		BootJsonView jv = new BootJsonView();
		jv.setPrettyPrint(bootJfishConfig.getMvc().getJson().isPrettyPrint());
		return jv;
	}
	
	/***
	 * instead of boot mapper config by JacksonAutoConfiguration
	 * @return
	 */
	@Bean
	public ObjectMapper objectMapper(){
		return BootWebUtils.createObjectMapper(applicationContext);
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
		resolver.setMaxUploadSize(bootSiteConfig.getUpload().getMaxUploadSize());
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
	
	/*@Bean
	@ConditionalOnProperty(prefix="deploy", name="server", havingValue="glassfish", matchIfMissing=false)
	public SimpleCharacterEncodingFilter fixGlassfishOrderedCharacterEncodingFilter(){
		SimpleCharacterEncodingFilter filter = new SimpleCharacterEncodingFilter();
		filter.setEncoding(this.httpEncodingProperties.getCharset().name());
		filter.setForceEncoding(this.httpEncodingProperties.isForce());
		filter.setOrder(Ordered.LOWEST_PRECEDENCE);

		logger.info("SimpleCharacterEncodingFilter init: {} ", this.httpEncodingProperties.getCharset().name());
		logger.info("SimpleCharacterEncodingFilter init: {} ", this.httpEncodingProperties.isForce());
		
		return filter;
	}*/

}
