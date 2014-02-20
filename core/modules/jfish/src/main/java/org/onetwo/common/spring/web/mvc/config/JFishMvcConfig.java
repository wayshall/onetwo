package org.onetwo.common.spring.web.mvc.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.onetwo.common.excel.XmlTemplateExcelViewResolver;
import org.onetwo.common.excel.view.jsp.DatagridExcelModelBuilder;
import org.onetwo.common.fish.plugin.JFishPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginManagerFactory;
import org.onetwo.common.fish.spring.config.JFishAppConfigrator;
import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.interfaces.XmlTemplateGeneratorFactory;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.spring.ftl.JFishFreeMarkerView;
import org.onetwo.common.spring.web.authentic.SpringAuthenticationInvocation;
import org.onetwo.common.spring.web.authentic.SpringSecurityInterceptor;
import org.onetwo.common.spring.web.mvc.CodeMessager;
import org.onetwo.common.spring.web.mvc.DefaultCodeMessager;
import org.onetwo.common.spring.web.mvc.JFishFirstInterceptor;
import org.onetwo.common.spring.web.mvc.JFishJaxb2Marshaller;
import org.onetwo.common.spring.web.mvc.ModelAndViewPostProcessInterceptor;
import org.onetwo.common.spring.web.mvc.MvcSetting;
import org.onetwo.common.spring.web.mvc.WebExceptionResolver;
import org.onetwo.common.spring.web.mvc.WebInterceptorAdapter;
import org.onetwo.common.spring.web.mvc.WebInterceptorAdapter.InterceptorOrder;
import org.onetwo.common.spring.web.mvc.annotation.JFishMvc;
import org.onetwo.common.spring.web.mvc.args.UserDetailArgumentResolver;
import org.onetwo.common.spring.web.mvc.args.WebAttributeArgumentResolver;
import org.onetwo.common.spring.web.mvc.log.AccessLogger;
import org.onetwo.common.spring.web.mvc.log.LoggerInterceptor;
import org.onetwo.common.spring.web.mvc.view.JsonExcelView;
import org.onetwo.common.spring.web.mvc.view.JsonView;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.validation.Validator;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.servlet.view.xml.MarshallingView;

/*******
 * 扩展mvc配置
 * @author wayshall
 *
 */
@Configuration
@JFishMvc
@ComponentScan(basePackageClasses = { JFishMvcConfig.class, SpringAuthenticationInvocation.class })
@ImportResource("classpath:mvc/spring-mvc.xml")
public class JFishMvcConfig extends WebMvcConfigurerAdapter implements InitializingBean, ApplicationContextAware {

	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
	
	@Resource
	private JFishMvcApplicationContext applicationContext;
	
	private JFishMvcConfigurerListenerManager listenerManager = new JFishMvcConfigurerListenerManager();
	
	@Autowired
	protected JFishPluginManager jfishPluginManager;

	@Autowired
	private JFishAppConfigrator jfishAppConfigurator;
	
	@Resource
	private MvcSetting mvcSetting;
	
	public JFishMvcConfig() {
//		jfishAppConfigurator = BaseSiteConfig.getInstance().getWebAppConfigurator(JFishAppConfigurator.class);
	}

	@Configuration
	static class MvcInterceptors {
		@Resource
		private JFishMvcApplicationContext applicationContext;
		
		JFishPluginManager pluginManager = JFishPluginManagerFactory.getPluginManager();

		@Bean
		public SpringSecurityInterceptor springSecurityInterceptor(){
			SpringSecurityInterceptor springSecurityInterceptor = SpringUtils.getHighestOrder(applicationContext, SpringSecurityInterceptor.class);
			if(springSecurityInterceptor==null){
				springSecurityInterceptor = new SpringSecurityInterceptor();
				springSecurityInterceptor.setOrder(InterceptorOrder.SECURITY);
			}
			return springSecurityInterceptor;
		}

		@Bean
		public MappedInterceptor mappedInterceptor4Security() {
//			SpringSecurityInterceptor springSecurityInterceptor = SpringUtils.getHighestOrder(applicationContext, SpringSecurityInterceptor.class);
//			if(springSecurityInterceptor==null){
//				springSecurityInterceptor = new SpringSecurityInterceptor();
//			}
//			SpringSecurityInterceptor springSecurityInterceptor = new SpringSecurityInterceptor();
			return new MappedInterceptor(null, springSecurityInterceptor());
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
			return WebInterceptorAdapter.createMappedInterceptor(new JFishFirstInterceptor());
		}

		@Bean
		public MappedInterceptor mappedLoggerInterceptor() {
			LoggerInterceptor loggerInterceptor = SpringUtils.getHighestOrder(applicationContext, LoggerInterceptor.class);
			if(loggerInterceptor==null){
				loggerInterceptor = new LoggerInterceptor();
				ContextHolder contextHolder = SpringUtils.getHighestOrder(applicationContext, ContextHolder.class);
				AccessLogger accessLogger = SpringUtils.getHighestOrder(applicationContext, AccessLogger.class);
				loggerInterceptor.setContextHolder(contextHolder);
				loggerInterceptor.setAccessLogger(accessLogger);
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

	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.registerCallableInterceptors(new TimeoutCallableProcessingInterceptor());
		configurer.setDefaultTimeout(10000);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		listenerManager.addListener((JFishMvcConfigurerListener)jfishPluginManager);
	}

	@Bean
	public JFishFreeMarkerConfigurer freeMarkerConfigurer() {
		final JFishFreeMarkerConfigurer freeMarker = new JFishFreeMarkerConfigurer(listenerManager);
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
		return prop;
	}

	@Bean(name = "mediaType")
	public Properties mediaType() {
		Properties prop = SpringUtils.createProperties("/mvc/media-type.properties", true);
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
	
	@Bean
	public InternalResourceViewResolver jspResolver(){
		InternalResourceViewResolver jspResoler = new InternalResourceViewResolver();
		jspResoler.setSuffix(".jsp");
		jspResoler.setPrefix("/WEB-INF/views/");
		return jspResoler;
	}
	
	@Bean
	public XmlTemplateExcelViewResolver excelResolver(){
		XmlTemplateExcelViewResolver resolver = new XmlTemplateExcelViewResolver();
		resolver.setViewClass(JsonExcelView.class);
		return resolver;
	}

	@Bean
	public DatagridExcelModelBuilder datagridExcelModelBuilder(){
		return new DatagridExcelModelBuilder();
	}
	
	@Bean
	public View jsonView() {
		JsonView jview = SpringUtils.getHighestOrder(applicationContext, JsonView.class);
		if(jview==null){
			jview = new JsonView();
		}
		return jview;
	}

	@Bean
	public View xmlView() {
		MarshallingView view = new MarshallingView();
		view.setMarshaller(jaxb2Marshaller());
		return view;
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
	public Jaxb2Marshaller jaxb2Marshaller() {
		JFishJaxb2Marshaller marshaller = new JFishJaxb2Marshaller();
		
		if(jfishAppConfigurator!=null && !LangUtils.isEmpty(jfishAppConfigurator.getXmlBasePackages())){
			marshaller.setClassesToBeBoundByBasePackages(jfishAppConfigurator.getXmlBasePackages());
		}else{
			String xmlBasePackage = this.mvcSetting.getMvcSetting().getProperty("xml.base.packages");
			Assert.hasText(xmlBasePackage, "xmlBasePackage in mvc.properties must has text");
			marshaller.setXmlBasePackage(xmlBasePackage);
		}
		return marshaller;
	}

	@Bean
	public ViewResolver contentNegotiatingViewResolver() {
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
		/*List<HttpMessageConverter<?>> converters = LangUtils.newArrayList();
		converters.add(new MappingJacksonHttpMessageConverter());
//		converters.add(new JsonStringHttpMessageConverter());
		ModelAndJsonCompatibleResolver modelAndJson = new ModelAndJsonCompatibleResolver(converters);
		argumentResolvers.add(modelAndJson);*/
		
		List<HandlerMethodArgumentResolver> resolvers = SpringUtils.getBeans(applicationContext, HandlerMethodArgumentResolver.class);
		argumentResolvers.addAll(resolvers);
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

	@Bean
	public HandlerExceptionResolver webExceptionResolver() {
		WebExceptionResolver webexception = SpringUtils.getHighestOrder(this.applicationContext, WebExceptionResolver.class);
		if(webexception==null){
			webexception = new WebExceptionResolver();
			webexception.setExceptionMessage(exceptionMessageSource());
			webexception.setMvcSetting(mvcSetting);
		}
		return webexception;
	}

	@Bean(name=MvcBeanNames.EXCEPTION_MESSAGE)
	public MessageSource exceptionMessageSource(){
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasenames("classpath:messages/ExceptionMessages", "classpath:messages/DefaultExceptionMessages");
		return ms;
	}
	
	@Bean
	public CodeMessager codeMessager(){
		CodeMessager messager = SpringUtils.getBean(applicationContext, CodeMessager.class);;
		if(messager==null){
			messager = new DefaultCodeMessager();
		}
		return messager;
	}
	
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(webExceptionResolver());
	}

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
		
		this.listenerManager.notifyAfterMvcConfig(applicationContext, this, peRegisttrarList);
		
		((ConfigurableWebBindingInitializer)requestMappingHandlerAdapter.getWebBindingInitializer()).setPropertyEditorRegistrars(peRegisttrarList.toArray(new PropertyEditorRegistrar[peRegisttrarList.size()]));

		SpringApplication.initApplication(applicationContext);
		
	}
	
	public Validator getValidator() {
		Validator validator = SpringApplication.getInstance().getBean(Validator.class);
		Assert.notNull(validator, "validator can not be null");
		return validator;
	}
	
	@Bean
	public XmlTemplateGeneratorFactory xmlTemplateGeneratorFactory(){
		/*String className = "org.onetwo.common.excel.POIExcelGeneratorImpl";
		DefaultXmlTemplateExcelFacotory factory = null;
		if(ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader())){
			factory = new DefaultXmlTemplateExcelFacotory();
//			factory.setCacheTemplate(true);
		}else{
			logger.warn("there is not bean implements [" + className + "]");
		}
		return factory;*/
		return excelResolver().getXmlTemplateGeneratorFactory();
	}
	

	public static class MvcBeanNames {
		public static final String EXCEPTION_MESSAGE = "exceptionMessages";
	}
	
	/*@Bean
	public DatagridExcelModelBuilder datagridExcelModelBuilder(){
		return new DatagridExcelModelBuilder();
	}*/
}
