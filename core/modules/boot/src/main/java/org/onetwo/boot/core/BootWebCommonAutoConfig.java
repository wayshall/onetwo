package org.onetwo.boot.core;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

import org.onetwo.boot.apiclient.ApiClientConfiguration;
import org.onetwo.boot.core.cleanup.UploadCleanupTimer;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.CleanupProps;
import org.onetwo.boot.core.config.BootSiteConfig.UploadConfig;
import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.boot.core.init.BootServletContextInitializer;
import org.onetwo.boot.core.init.ConfigServletContextInitializer;
import org.onetwo.boot.core.json.BootJackson2ObjectMapperBuilder;
import org.onetwo.boot.core.json.ObjectMapperProvider;
import org.onetwo.boot.core.json.ObjectMapperProvider.DefaultObjectMapperProvider;
import org.onetwo.boot.core.listener.BootApplicationReadyListener;
import org.onetwo.boot.core.listener.PropertyDebuggerListener;
import org.onetwo.boot.core.web.BootMvcConfigurerAdapter;
import org.onetwo.boot.core.web.api.WebApiRequestMappingCombiner;
import org.onetwo.boot.core.web.filter.HostPreventFilter;
import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorManager;
import org.onetwo.boot.core.web.mvc.interceptor.UploadValidateInterceptor;
import org.onetwo.boot.core.web.userdetails.BootSessionUserManager;
import org.onetwo.boot.core.web.view.BootJsonView;
import org.onetwo.boot.core.web.view.ExtJackson2HttpMessageConverter;
import org.onetwo.boot.core.web.view.MvcViewRender;
import org.onetwo.boot.core.web.view.ResultBodyAdvice;
import org.onetwo.boot.core.web.view.XResponseViewManager;
import org.onetwo.boot.dsrouter.DsRouterConfiguration;
import org.onetwo.boot.utils.BootUtils;
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.SimpleFileStorer;
import org.onetwo.common.ftp.FtpClientManager.FtpConfig;
import org.onetwo.common.ftp.FtpFileStorer;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.web.userdetails.GenericUserDetail;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

/***
 * web环境的通用配置
 * 
 * 可引入自动修改一些默认配置的配置：BootFixedConfiguration
 * 
 * @author wayshall
 *
 */
@Import({DsRouterConfiguration.class, ApiClientConfiguration.class, BootConfigurationForFixSpringBoot2x.class})
public class BootWebCommonAutoConfig implements DisposableBean {
	public static final String BEAN_NAME_EXCEPTION_RESOLVER = "bootWebExceptionResolver";
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected ApplicationContext applicationContext;

	@Autowired
	protected BootSpringConfig bootSpringConfig;

	@Autowired
	protected BootJFishConfig bootJfishConfig;
	
	@Autowired
	protected BootSiteConfig bootSiteConfig;

//	@Autowired
//	private MultipartProperties multipartProperties;
	
	@Autowired
	protected BootJsonView jsonView;
	
	@PostConstruct
	public void init(){
		Springs.initApplicationIfNotInitialized(applicationContext);
	}

	/* @see BootFixedConfiguration
	 * @Bean
	@ConditionalOnProperty(value=TomcatProperties.ENABLED_CUSTOMIZER_TOMCAT, matchIfMissing=true, havingValue="true")
	public BootServletContainerCustomizer bootServletContainerCustomizer(){
		return new BootServletContainerCustomizer();
	}*/
	
	@Override
	public void destroy() throws Exception {
		BootUtils.sutdownAsyncInitor();
	}
	
	@Bean
	public BootApplicationReadyListener bootApplicationReadyListener() {
		return new BootApplicationReadyListener();
	}

	@Bean
	public MvcViewRender mvcViewRender(){
		return new MvcViewRender();
	}
	
	/*@Bean
	public SpringMultipartFilterProxy springMultipartFilterProxy() {
		return new SpringMultipartFilterProxy();
	}*/
	
//	@Bean
//	public FilterRegistrationBean requestContextFilter(){
//		FilterRegistrationBean registration = new FilterRegistrationBean(new BootRequestContextFilter());
//		registration.setOrder(Ordered.HIGHEST_PRECEDENCE+100);
//		registration.setName("requestContextFilter");
//		return registration;
//	}
	
	/***
	 * 注册自定义filter
	 * @author wayshall
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(BootServletContextInitializer.class)
	public BootServletContextInitializer bootServletContextInitializer(){
		return new BootServletContextInitializer();
	}
	
	@Bean
	public ConfigServletContextInitializer configServletContextInitializer(){
		return new ConfigServletContextInitializer();
	}
	
	/****
	 * 扩展requestmapping，增加监听器
	 * @author wayshall
	 * @return
	 */
	/*@Bean
	public RequestMappingHandlerMappingListenable requestMappingHandlerMappingListenable(){
		RequestMappingHandlerMappingListenable req = new RequestMappingHandlerMappingListenable();
		return req;	
	}*/
	
	@Bean
	public BootMvcConfigurerAdapter bootMvcConfigurerAdapter(){
		return new BootMvcConfigurerAdapter();
	}

	/***
	 * 覆盖 spring的RequestMappingHandlerMapping 实现
	 * @author wayshall
	 * @return
	 */
	/*@Bean
	@ConditionalOnMissingBean(BootWebMvcRegistrations.class)
	public BootWebMvcRegistrations bootWebMvcRegistrations(){
		return new BootWebMvcRegistrations();
	}*/
	
	@Bean
	public WebApiRequestMappingCombiner webApiRequestMappingCombiner(){
		return new WebApiRequestMappingCombiner();
	}
	
	/***
	 * @author wayshall
	 * @return
	 */
	@Bean
	public ExtJackson2HttpMessageConverter extJackson2HttpMessageConverter(){
		return new ExtJackson2HttpMessageConverter();
	}
	
	/*@Bean
	public BootWebExceptionHandler bootWebExceptionHandler(){
		return new BootWebExceptionHandler();
	}*/
	
	/***
	 * 拦截器
	 * @return
	 */
	@Bean
	public BootFirstInterceptor bootFirstInterceptor(){
		return new BootFirstInterceptor();
	}
	
	@Bean
	public MvcInterceptorManager mvcInterceptorManager(){
		return new MvcInterceptorManager();
	}

	@Bean
	public UploadValidateInterceptor uploadValidateInterceptor(){
		return new UploadValidateInterceptor();
	}
	
	@Bean
	public ResultBodyAdvice resultBodyAdvice(){
		return new ResultBodyAdvice();
	}
	
	@Bean
	public XResponseViewManager xresponseViewManager(){
		XResponseViewManager viewManager = new XResponseViewManager();
		viewManager.setAlwaysWrapDataResult(bootJfishConfig.getMvc().getJson().isAlwaysWrapDataResult());
		return viewManager;
	}

	@Bean
	@ConditionalOnMissingBean(SessionUserManager.class)
	public SessionUserManager<GenericUserDetail<?>> sessionUserManager(){
		return new BootSessionUserManager();
	}

	

	/****
	 * 通过配置site.upload.fileStorePath启用
	 * site.upload.storeType存储方式：本地或者ftp
	 * site.upload.fileStorePath本地路径
	 * site.upload.appContextDir应用子目录
	 * @author wayshall
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="local")
	public FileStorer localStorer(){
		UploadConfig config = bootSiteConfig.getUpload();
		SimpleFileStorer fs = new SimpleFileStorer();
		fs.setStoreBaseDir(config.getFileStorePath());//site.upload.fileStorePath
//		fs.setAppContextDir(config.getAppContextDir());//site.upload.appContextDir
		return fs;
	}
	
	@Bean
	@ConditionalOnProperty(name=BootSiteConfig.ENABLE_STORETYPE_PROPERTY, havingValue="ftp")
	public FileStorer ftpStorer(){
		UploadConfig config = bootSiteConfig.getUpload();
		FtpConfig ftpConfig = new FtpConfig();
		ftpConfig.setEncoding(config.getFtpEncoding());
		ftpConfig.setServer(config.getFtpServer());
		ftpConfig.setPort(config.getFtpPort());
		
		FtpFileStorer fs = new FtpFileStorer(ftpConfig);
		fs.setLoginParam(config.getFtpUser(), config.getFtpPassword());
//		fs.setStoreBaseDir(config.getFtpBaseDir());
		fs.setStoreBaseDir(config.getFileStorePath());
//		fs.setAppContextDir(config.getAppContextDir());
		return fs;
	}

	@ConditionalOnProperty(name = CleanupProps.ENABLED_KEY, havingValue = "true", matchIfMissing = false)
	@Bean
	public UploadCleanupTimer uploadCleanupTimer() {
		return new UploadCleanupTimer();
	}

	@ConditionalOnProperty(name=BootJFishConfig.PROFILES_DEBUG)
	public PropertyDebuggerListener propertyDebuggerListener() {
		return new PropertyDebuggerListener();
	}
	
	/****
	 * CorsFilter 须在所有filter之前，包括security的filter
	 * 否则会抛 No 'Access-Control-Allow-Origin' header is present on the requested resource
	 * filter
	 * 
	 * deprecated 被CorsFilterConfiguration取代
	 * @return
	 */
//	@Bean
//	@ConditionalOnProperty(name=BootJFishConfig.ENABLE_CORSFILTER, havingValue="true", matchIfMissing=false)
//	public FilterRegistrationBean<?> corsFilterRegistration(@Qualifier(CorsFilter.CORS_FILTER_NAME) Filter filter){
//		FilterRegistrationBean<?> registration = new FilterRegistrationBean<>(filter);
//		registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
//		registration.setName(CorsFilter.CORS_FILTER_NAME);
//		return registration;
//	}

//	@ConditionalOnProperty(name=BootJFishConfig.ENABLE_CORSFILTER, havingValue="true", matchIfMissing=false)
//	@Bean(name = CorsFilter.CORS_FILTER_NAME)
//	public CorsFilter corsFilter(){
//		CorsFilter filter = new CorsFilter();
//		return filter;
//	}

	@Bean
//	@ConditionalOnBean(name = HostPreventFilter.FILTER_NAME)
	@ConditionalOnProperty(name=BootJFishConfig.ENABLE_HOST_FILTER, havingValue="true", matchIfMissing=true)
	public FilterRegistrationBean<?> hostPreventFilterFilterRegistration(@Qualifier(HostPreventFilter.FILTER_NAME) Filter filter){
		FilterRegistrationBean<?> registration = new FilterRegistrationBean<>(filter);
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
		registration.setName(HostPreventFilter.FILTER_NAME);
		return registration;
	}

	@ConditionalOnProperty(name=BootJFishConfig.ENABLE_HOST_FILTER, havingValue="true", matchIfMissing=true)
	@Bean(name = HostPreventFilter.FILTER_NAME)
	public HostPreventFilter hostPreventFilter(){
		HostPreventFilter filter = new HostPreventFilter();
		return filter;
	}

	
	@Configuration
	protected static class JsonConfiguration {
		@Autowired
		protected BootJFishConfig bootJfishConfig;
		@Bean
		public BootJsonView bootJsonView(){
			BootJsonView jv = new BootJsonView();
			jv.setPrettyPrint(bootJfishConfig.getMvc().getJson().isPrettyPrint());
			return jv;
		}
		
		@Bean
		@ConditionalOnMissingBean(ObjectMapperProvider.class)
		public ObjectMapperProvider objectMapperProvider(){
			return new DefaultObjectMapperProvider();
		}

		@Primary
		@Bean
		@ConditionalOnMissingBean(ObjectMapper.class)
		public ObjectMapper objectMapper(){
			return objectMapperProvider().createObjectMapper();
		}
		
		@Bean
		public BootJackson2ObjectMapperBuilder bootJackson2ObjectMapperBuilder(){
			return new BootJackson2ObjectMapperBuilder();
		}
	}
	
	@Configuration
	protected static class ConfigureMessageConvertor {
		private List<RequestMappingHandlerAdapter> handlerAdapters;
		
		public ConfigureMessageConvertor(List<RequestMappingHandlerAdapter> handlerAdapters) {
			this.handlerAdapters = handlerAdapters;
		}

		@PostConstruct
		public void configureHttpMessageConverters() {
			for (RequestMappingHandlerAdapter handlerAdapter : this.handlerAdapters) {
				AnnotationAwareOrderComparator.sort(handlerAdapter.getMessageConverters());
			}
		}
	}
}
