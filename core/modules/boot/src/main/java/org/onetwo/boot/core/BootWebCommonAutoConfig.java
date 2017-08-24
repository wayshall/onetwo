package org.onetwo.boot.core;

import javax.annotation.PostConstruct;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.StoreType;
import org.onetwo.boot.core.config.BootSiteConfig.UploadConfig;
import org.onetwo.boot.core.init.BootServletContextInitializer;
import org.onetwo.boot.core.init.ConfigServletContextInitializer;
import org.onetwo.boot.core.json.BootJackson2ObjectMapperBuilder;
import org.onetwo.boot.core.web.BootMvcConfigurerAdapter;
import org.onetwo.boot.core.web.filter.BootRequestContextFilter;
import org.onetwo.boot.core.web.mvc.BootStandardServletMultipartResolver;
import org.onetwo.boot.core.web.mvc.RequestMappingHandlerMappingListenable;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionHandler;
import org.onetwo.boot.core.web.mvc.interceptor.BootFirstInterceptor;
import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorManager;
import org.onetwo.boot.core.web.mvc.interceptor.UploadValidateInterceptor;
import org.onetwo.boot.core.web.userdetails.BootSessionUserManager;
import org.onetwo.boot.core.web.view.AutoWrapJackson2HttpMessageConverter;
import org.onetwo.boot.core.web.view.BootJsonView;
import org.onetwo.boot.core.web.view.ResultBodyAdvice;
import org.onetwo.boot.core.web.view.XResponseViewManager;
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
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/***
 * web环境的通用配置
 * @author wayshall
 *
 */
public class BootWebCommonAutoConfig {
	public static final String BEAN_NAME_EXCEPTION_RESOLVER = "bootWebExceptionResolver";
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected ApplicationContext applicationContext;

	@Autowired
	protected BootJFishConfig bootJfishConfig;
	
	@Autowired
	protected BootSiteConfig bootSiteConfig;
	
	@PostConstruct
	public void init(){
		Springs.initApplicationIfNotInitialized(applicationContext);
	}

	
	/***
	 * 异常解释
	 * @return
	 */
//	@Bean(BootWebCommonAutoConfig.BEAN_NAME_EXCEPTION_RESOLVER)
	@Bean
	public ResponseEntityExceptionHandler responseEntityExceptionHandler(){
		BootWebExceptionHandler handler = new BootWebExceptionHandler();
		return handler;
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
	@Bean
	public RequestMappingHandlerMappingListenable requestMappingHandlerMappingListenable(){
		RequestMappingHandlerMappingListenable req = new RequestMappingHandlerMappingListenable();
		return req;	
	}
	
	@Bean
	public BootMvcConfigurerAdapter bootMvcConfigurerAdapter(){
		return new BootMvcConfigurerAdapter();
	}
	
	/***
	 * 暂时屏蔽
	 * @author wayshall
	 * @return
	 */
//	@Bean
	public AutoWrapJackson2HttpMessageConverter autoWrapJackson2HttpMessageConverter(){
		return new AutoWrapJackson2HttpMessageConverter(bootJfishConfig.getMvc().getAutoWrapResult());
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
	public BootJsonView bootJsonView(){
		BootJsonView jv = new BootJsonView();
		jv.setPrettyPrint(bootJfishConfig.getMvc().getJson().isPrettyPrint());
		return jv;
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
	
	@Bean(name=MultipartFilter.DEFAULT_MULTIPART_RESOLVER_BEAN_NAME)
//	@ConditionalOnMissingBean(MultipartResolver.class)
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

	@Bean
	public BootJackson2ObjectMapperBuilder bootJackson2ObjectMapperBuilder(){
		return new BootJackson2ObjectMapperBuilder();
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
	@ConditionalOnMissingBean(FileStorer.class)
//	@ConditionalOnBean(AbstractBaseController.class)
	public FileStorer<?> fileStorer(){
		UploadConfig config = bootSiteConfig.getUpload();
		StoreType type = config.getStoreType();//site.upload.storeType
		
		if(type==StoreType.LOCAL){
			SimpleFileStorer fs = new SimpleFileStorer();
			fs.setStoreBaseDir(config.getFileStorePath());//site.upload.fileStorePath
			fs.setAppContextDir(config.getAppContextDir());//site.upload.appContextDir
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
	
}
