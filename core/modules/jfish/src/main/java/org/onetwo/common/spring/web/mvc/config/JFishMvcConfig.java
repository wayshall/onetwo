package org.onetwo.common.spring.web.mvc.config;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.xml.transform.Source;

import org.onetwo.common.fish.plugin.JFishWebMvcPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.fish.spring.config.JFishContextConfig.ContextBeanNames;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.web.mvc.EmptySecurityInterceptor;
import org.onetwo.common.spring.web.mvc.JFishFirstInterceptor;
import org.onetwo.common.spring.web.mvc.ModelAndViewPostProcessInterceptor;
import org.onetwo.common.spring.web.mvc.MvcSetting;
import org.onetwo.common.spring.web.mvc.SecurityInterceptor;
import org.onetwo.common.spring.web.mvc.WebExceptionResolver;
import org.onetwo.common.spring.web.mvc.WebInterceptorAdapter;
import org.onetwo.common.spring.web.mvc.annotation.JFishMvc;
import org.onetwo.common.spring.web.mvc.args.AsyncWebProcessorArgumentResolver;
import org.onetwo.common.spring.web.mvc.args.ListParameterArgumentResolver;
import org.onetwo.common.spring.web.mvc.args.UserDetailArgumentResolver;
import org.onetwo.common.spring.web.mvc.args.WebAttributeArgumentResolver;
import org.onetwo.common.spring.web.mvc.log.LoggerInterceptor;
import org.onetwo.common.spring.web.reqvalidator.JFishRequestValidator;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.Validator;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

/*******
 * 扩展mvc配置
 * @author wayshall
 *
 */
@Configuration
@JFishMvc
//@ComponentScan(basePackageClasses = { JFishMvcConfig.class, SpringAuthenticationInvocation.class })
//@ComponentScan(basePackageClasses = { JFishMvcConfig.class })
@ImportResource("classpath:mvc/spring-mvc.xml")
public class JFishMvcConfig extends WebMvcConfigurerAdapter implements InitializingBean, ApplicationContextAware {

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
	
	@Resource
	private JFishMvcApplicationContext applicationContext;
	
	
	protected JFishWebMvcPluginManager jfishPluginManager = JFishPluginManagerFactory.getPluginManager();

//	@Autowired
//	private JFishAppConfigrator jfishAppConfigurator;
	
	@Resource
	private MvcSetting mvcSetting;

	@Resource(name=ContextBeanNames.EXCEPTION_MESSAGE)
	private MessageSource exceptionMessages;
	
	private BaseSiteConfig siteConfig = BaseSiteConfig.getInstance();
	
	public JFishMvcConfig() {
//		jfishAppConfigurator = BaseSiteConfig.getInstance().getWebAppConfigurator(JFishAppConfigurator.class);
//		listenerManager.addListener((JFishMvcConfigurerListener)jfishPluginManager);
//		jfishPluginManager.getMvcEventBus().registerListenerByPluginManager(jfishPluginManager);
	}

	@Configuration
	static class MvcInterceptors {
		@Resource
		private JFishMvcApplicationContext applicationContext;
		
//		@Resource
//		private SecurityInterceptor securityInterceptor;
		
		private JFishWebMvcPluginManager pluginManager = JFishPluginManagerFactory.getPluginManager();

		/**@Bean
		public SecurityInterceptor securityInterceptor(){
			SecurityInterceptor springSecurityInterceptor = SpringUtils.getHighestOrder(applicationContext, SecurityInterceptor.class);
			if(springSecurityInterceptor==null){
				springSecurityInterceptor = new EmptySecurityInterceptor();
			}
			return springSecurityInterceptor;
		}**/

		@Bean
		public MappedInterceptor mappedInterceptor4Security() {
			SecurityInterceptor securityInterceptor = SpringUtils.getHighestOrder(applicationContext, SecurityInterceptor.class);
			if(securityInterceptor==null){
				securityInterceptor = new EmptySecurityInterceptor();
			}
//			SpringSecurityInterceptor springSecurityInterceptor = new SpringSecurityInterceptor();
			//return new MappedInterceptor(null, securityInterceptor());
			return new MappedInterceptor(null, securityInterceptor);
		}

		/************
		 * spring mvc will scan {@code MappedInterceptor}
		 * @return
		 */
		@Bean
		public MappedInterceptor mappedInterceptor4ModelAndViewPostProcess() {
			ModelAndViewPostProcessInterceptor post = SpringUtils.getHighestOrder(applicationContext, ModelAndViewPostProcessInterceptor.class);
			if(post!=null){
				post.setPluginManager(pluginManager);
				return WebInterceptorAdapter.createMappedInterceptor(post);
			}
			return WebInterceptorAdapter.createMappedInterceptor(new ModelAndViewPostProcessInterceptor(pluginManager));
		}

		@Bean
		public MappedInterceptor mappedInterceptor4First() {
			List<JFishRequestValidator> validators = SpringUtils.getBeans(applicationContext, JFishRequestValidator.class);
			return WebInterceptorAdapter.createMappedInterceptor(new JFishFirstInterceptor(validators));
		}

		@Bean
		public MappedInterceptor loggerInterceptor() {
			LoggerInterceptor loggerInterceptor = SpringUtils.getHighestOrder(applicationContext, LoggerInterceptor.class);
			if(loggerInterceptor==null){
				loggerInterceptor = new LoggerInterceptor();
				SpringUtils.injectAndInitialize(applicationContext, loggerInterceptor);
				/*ContextHolder contextHolder = SpringUtils.getHighestOrder(applicationContext, ContextHolder.class);
				AccessLogger accessLogger = SpringUtils.getHighestOrder(applicationContext, AccessLogger.class);
				loggerInterceptor.setContextHolder(contextHolder);
				loggerInterceptor.setAccessLogger(accessLogger);*/
			}
			return WebInterceptorAdapter.createMappedInterceptor(loggerInterceptor);
		}

		/*@Bean
		public MappedInterceptor mappedInterceptor4Security() {
			try {
				return (MappedInterceptor) securityInterceptorFacotryBean().getObject();
			} catch (Exception e) {
				throw new BaseException("create security interceptor error : " + e.getMessage(), e);
			}
		}*/
	}

	@Bean
	public JFishWebMvcPluginManager jfishPluginManager(){
		return this.jfishPluginManager;
	}
	
//	@Bean 
	protected AsyncTaskExecutor mvcAsyncTaskExecutor(){
		String taskExecutorName = this.mvcSetting.getAsyncTaskExecutor();
		AsyncTaskExecutor taskExecutor = null;
		if(StringUtils.isNotBlank(taskExecutorName)){
			taskExecutor = SpringUtils.getBean(applicationContext, taskExecutorName);
			if(taskExecutor!=null){
				return taskExecutor;
			}
		}
		/****
		 * <property name="corePoolSize" value="5" /><!--最小线程数 -->  
	        <property name="maxPoolSize" value="10" /><!--最大线程数 -->  
	        <property name="queueCapacity" value="50" /><!--缓冲队列大小 -->  
	        <property name="threadNamePrefix" value="abc-" /><!--线程池中产生的线程名字前缀 -->  
	        <property name="keepAliveSeconds" value="30" /><!--线程池中空闲线程的存活时间单位秒 -->  
		 */
		ThreadPoolTaskExecutor execotor = new ThreadPoolTaskExecutor();
		execotor.setCorePoolSize(5);
		execotor.setMaxPoolSize(20);
//			execotor.setQueueCapacity(queueCapacity);
//			execotor.setKeepAliveSeconds(60);
		execotor.setThreadNamePrefix("jfish-");
		taskExecutor = execotor;
		
		SpringUtils.injectAndInitialize(applicationContext, taskExecutor);
		SpringUtils.registerSingleton(applicationContext, "mvcAsyncTaskExecutor", taskExecutor);
		return taskExecutor;
	}
	
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		if(this.mvcSetting.isAsyncSupported()){
			configurer.registerCallableInterceptors(new TimeoutCallableProcessingInterceptor());
			configurer.setDefaultTimeout(TimeUnit.SECONDS.toMillis(10*60));
			AsyncTaskExecutor taskExecutor = mvcAsyncTaskExecutor();
			configurer.setTaskExecutor(taskExecutor);
		}
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	}
/*
	@Bean
	public JFishFreeMarkerConfigurer freeMarkerConfigurer() {
		final JFishFreeMarkerConfigurer freeMarker = new JFishFreeMarkerConfigurer(this.jfishPluginManager.getMvcEventBus());
		final List<String> templatePaths = new ArrayList<String>(3);
		templatePaths.add("/WEB-INF/views/");
		templatePaths.add("/WEB-INF/ftl/");
		final Properties setting = freemarkerSetting();
		freeMarker.setTemplateLoaderPaths(templatePaths.toArray(new String[templatePaths.size()]));
		freeMarker.setDefaultEncoding("UTF-8");
		freeMarker.setFreemarkerSettings(setting);
		freeMarker.setJfishPluginManager(jfishPluginManager);
		
		return freeMarker;
	}

	@Bean(name = "freemarkerSetting")
	public Properties freemarkerSetting() {
		Properties prop = SpringUtils.createProperties("/mvc/freemarker.properties", true);
		if(!prop.containsKey(FtlUtils.CONFIG_CLASSIC_COMPATIBLE)){
			prop.setProperty(FtlUtils.CONFIG_CLASSIC_COMPATIBLE, "true");
		}
		return prop;
	}
	
	
	@Bean
	public FreeMarkerViewResolver freeMarkerViewResolver() {
		FreeMarkerViewResolver fmResolver = new FreeMarkerViewResolver();
		fmResolver.setViewClass(JFishFreeMarkerView.class);
		fmResolver.setSuffix(".ftl");
		fmResolver.setContentType("text/html;charset=UTF-8");
		fmResolver.setExposeRequestAttributes(true);
		fmResolver.setExposeSessionAttributes(true);
		fmResolver.setExposeSpringMacroHelpers(true);
		fmResolver.setRequestContextAttribute("request");
		fmResolver.setOrder(1);
		return fmResolver;
	}
	
	*/

	@Bean(name = "mediaType")
	public Properties mediaType() {
		Properties prop = SpringUtils.createProperties("/mvc/media-type.properties", true);
		return prop;
	}
	
	
	
	/*@Bean
	public View excelView(){
		JsonExcelView view = new JsonExcelView();
		view.setModelGeneratorFactory((ModelGeneratorFactory)xmlTemplateGeneratorFactory());
		return view;
	}*/

	/*@Bean
	public View jfishExcelView() {
		JFishDownloadView view = new JFishDownloadView();
		return view;
	}*/


	@Bean
	//dispecher
//	public ViewResolver contentNegotiatingViewResolver() {
	public ViewResolver viewResolver() {
		ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
		viewResolver.setUseNotAcceptableStatusCode(true);
		viewResolver.setOrder(0);
//		List<View> views = LangUtils.asListWithType(View.class, xmlView(), jsonView());
		List<View> views = SpringUtils.getBeans(applicationContext, View.class);
		viewResolver.setDefaultViews(views);
//		viewResolver.setMediaTypes(mediaType());
//		viewResolver.setDefaultContentType(MediaType.TEXT_HTML);
//		viewResolver.setIgnoreAcceptHeader(true);
		viewResolver.setContentNegotiationManager(contentNegotiationManagerFactoryBean().getObject());
		return viewResolver;
	}
	
	@Bean
	public ContentNegotiationManagerFactoryBean contentNegotiationManagerFactoryBean(){
		ContentNegotiationManagerFactoryBean bean = new ContentNegotiationManagerFactoryBean();
		bean.setMediaTypes(mediaType());
		bean.setDefaultContentType(MediaType.TEXT_HTML);
		bean.setIgnoreAcceptHeader(true);
		bean.setFavorParameter(true);
		return bean;
	}

	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new UserDetailArgumentResolver());
		argumentResolvers.add(new WebAttributeArgumentResolver());
		argumentResolvers.add(new ListParameterArgumentResolver());
		argumentResolvers.add(new AsyncWebProcessorArgumentResolver());
		/*List<HttpMessageConverter<?>> converters = LangUtils.newArrayList();
		converters.add(new MappingJacksonHttpMessageConverter());
//		converters.add(new JsonStringHttpMessageConverter());
		ModelAndJsonCompatibleResolver modelAndJson = new ModelAndJsonCompatibleResolver(converters);
		argumentResolvers.add(modelAndJson);*/
		
/*		List<HandlerMethodArgumentResolver> resolvers = SpringUtils.getBeans(applicationContext, HandlerMethodArgumentResolver.class);
		argumentResolvers.addAll(resolvers);*/
		this.jfishPluginManager.getMvcEventBus().postArgumentResolversRegisteEvent(argumentResolvers);
	}
	

	/*@Bean
	public HandlerMethodArgumentResolver webArgumentResolver() {
		JFishWebArgumentResolver webArgs = new JFishWebArgumentResolver();
		return webArgs;
	}

	@Bean
	public HandlerMethodArgumentResolver webAttributeArgumentResolver() {
		WebAttributeArgumentResolver webattr = new WebAttributeArgumentResolver();
		return webattr;
	}*/
	

	/****
	 * configureHandlerExceptionResolvers 和 webExceptionResolver定义，二选一即可
	 */
	/*public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(webExceptionResolver());
	}*/

	@Bean(name=DispatcherServlet.HANDLER_EXCEPTION_RESOLVER_BEAN_NAME)
	public HandlerExceptionResolver webExceptionResolver() {
		//如果已有相同名字的bean，会自动忽略，下面的代码多余。
		/*WebExceptionResolver webexception = SpringUtils.getHighestOrder(this.applicationContext, WebExceptionResolver.class);
		if(webexception==null){
			webexception = new WebExceptionResolver();
		}*/
		WebExceptionResolver webexception = new WebExceptionResolver();
//		webexception.setExceptionMessage(exceptionMessageSource());
		webexception.setExceptionMessage(exceptionMessages);
		webexception.setMvcSetting(mvcSetting);
		return webexception;
	}

	/*@Bean(name=MvcBeanNames.EXCEPTION_MESSAGE)
	public MessageSource exceptionMessageSource(){
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasenames("classpath:messages/ExceptionMessages", "classpath:messages/DefaultExceptionMessages");
		return ms;
	}*/
	

	public void afterPropertiesSet() throws Exception{
//		this.jfishAppConfigurator = SpringUtils.getBean(applicationContext, JFishAppConfigrator.class);
//		Assert.notNull(jfishAppConfigurator, "there is not app configurator");
		
		final List<PropertyEditorRegistrar> peRegisttrarList = new JFishList<PropertyEditorRegistrar>();
		/*peRegisttrarList.add(new PropertyEditorRegistrar() {
			@Override
			public void registerCustomEditors(PropertyEditorRegistry registry) {
				registry.registerCustomEditor(Enum.class, new EnumEditor());
			}
		});*/
		
		this.jfishPluginManager.getMvcEventBus().postAfterMvcConfig(applicationContext, jfishPluginManager, this, peRegisttrarList);
		
		((ConfigurableWebBindingInitializer)requestMappingHandlerAdapter.getWebBindingInitializer()).setPropertyEditorRegistrars(peRegisttrarList.toArray(new PropertyEditorRegistrar[peRegisttrarList.size()]));

		SpringApplication.initApplication(applicationContext);
		
	}
	
	public Validator getValidator() {
		Validator validator = SpringApplication.getInstance().getBean(Validator.class);
		Assert.notNull(validator, "validator can not be null");
		return validator;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setWriteAcceptCharset(false);

		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(stringConverter);
		messageConverters.add(new ResourceHttpMessageConverter());
		messageConverters.add(new SourceHttpMessageConverter<Source>());
		messageConverters.add(new AllEncompassingFormHttpMessageConverter());

		/*if (romePresent) {
			messageConverters.add(new AtomFeedHttpMessageConverter());
			messageConverters.add(new RssChannelHttpMessageConverter());
		}
		if (jaxb2Present) {
			messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
		}
		if (jackson2Present) {
			messageConverters.add(new MappingJackson2HttpMessageConverter());
		}
		else if (jacksonPresent) {
			messageConverters.add(new MappingJacksonHttpMessageConverter());
		}*/
		if (siteConfig.isViewJsonXmlSupported()) {
			messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			
			ObjectMapper mapper = JsonMapper.ignoreNull().getObjectMapper();
			Module module = SpringApplication.getInstance().getBean(Module.class);
//			h4m.disable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);
//			h4m.enable(Hibernate4Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
			if(module!=null)
				mapper.registerModule(module);
			converter.setObjectMapper(mapper);
			
			messageConverters.add(converter);
		}
	}
	

	/*public static class MvcBeanNames {
		public static final String EXCEPTION_MESSAGE = "exceptionMessages";
	}*/
	
	/*@Bean
	public DatagridExcelModelBuilder datagridExcelModelBuilder(){
		return new DatagridExcelModelBuilder();
	}*/
}
