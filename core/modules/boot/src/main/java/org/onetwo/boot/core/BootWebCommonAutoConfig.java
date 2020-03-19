package org.onetwo.boot.core;

import java.util.List;

import javax.annotation.PostConstruct;

import org.onetwo.boot.apiclient.ApiClientConfiguration;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.UploadConfig;
import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.boot.core.init.BootServletContextInitializer;
import org.onetwo.boot.core.init.ConfigServletContextInitializer;
import org.onetwo.boot.core.json.BootJackson2ObjectMapperBuilder;
import org.onetwo.boot.core.json.ObjectMapperProvider;
import org.onetwo.boot.core.json.ObjectMapperProvider.DefaultObjectMapperProvider;
import org.onetwo.boot.core.web.BootMvcConfigurerAdapter;
import org.onetwo.boot.core.web.api.WebApiRequestMappingCombiner;
import org.onetwo.boot.core.web.filter.BootRequestContextFilter;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionResolver;
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
import org.onetwo.common.file.FileStorer;
import org.onetwo.common.file.SimpleFileStorer;
import org.onetwo.common.ftp.FtpClientManager.FtpConfig;
import org.onetwo.common.ftp.FtpFileStorer;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.web.userdetails.SessionUserManager;
import org.onetwo.common.web.userdetails.UserDetail;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author wayshall
 *
 */
@Import({DsRouterConfiguration.class, ApiClientConfiguration.class})
public class BootWebCommonAutoConfig {
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
	private BootJsonView jsonView;
	
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
	
	@Bean
	public MvcViewRender mvcViewRender(){
		return new MvcViewRender();
	}
	
	/*@Bean
	public SpringMultipartFilterProxy springMultipartFilterProxy() {
		return new SpringMultipartFilterProxy();
	}*/
	
	/***
	 * 异常解释
	 * @return
	 */
	@Bean(BootWebCommonAutoConfig.BEAN_NAME_EXCEPTION_RESOLVER)
//	@ConditionalOnMissingBean({BootWebExceptionResolver.class, ResponseEntityExceptionHandler.class})
//	@Autowired
	@ConditionalOnMissingBean({BootWebExceptionResolver.class})
	public BootWebExceptionResolver bootWebExceptionResolver(){
		BootWebExceptionResolver resolver = new BootWebExceptionResolver();
//		resolver.setExceptionMessage(exceptionMessage);
		resolver.setJfishConfig(bootJfishConfig);
		resolver.setErrorView(jsonView);
		return resolver;
	}
	
	@Bean
	public FilterRegistrationBean requestContextFilter(){
		FilterRegistrationBean registration = new FilterRegistrationBean(new BootRequestContextFilter());
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE+100);
		registration.setName("requestContextFilter");
		return registration;
	}
	
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
	
	/*
	 * @see BootFixedConfiguration
	 * @Bean(name=MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME)
//	@ConditionalOnMissingBean(MultipartResolver.class)
	public MultipartResolver filterMultipartResolver(){
		BootStandardServletMultipartResolver resolver = new BootStandardServletMultipartResolver();
		resolver.setMaxUploadSize(FileUtils.parseSize(multipartProperties.getMaxRequestSize()));
		return resolver;
	}*/

	@Bean
	@ConditionalOnMissingBean(SessionUserManager.class)
	public SessionUserManager<UserDetail> sessionUserManager(){
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
