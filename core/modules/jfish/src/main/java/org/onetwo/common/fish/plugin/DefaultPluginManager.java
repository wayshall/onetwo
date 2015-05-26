package org.onetwo.common.fish.plugin;

import java.util.Comparator;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.plugin.ContextPlugin;
import org.onetwo.common.spring.plugin.EmptyContextPlugin;
import org.onetwo.common.spring.plugin.PluginInfo;
import org.onetwo.common.spring.plugin.SpringContextPluginManager;
import org.onetwo.common.spring.web.mvc.config.event.JFishMvcEventBus;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.MapIt;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.slf4j.Logger;
import org.springframework.web.context.WebApplicationContext;

public class DefaultPluginManager extends SpringContextPluginManager<JFishPluginMeta> implements JFishPluginManager {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	private PluginNameParser pluginNameParser = PLUGINNAME_PARSER;
	final private JFishMvcEventBus mvcEventBus = new JFishMvcEventBus(this);

	public DefaultPluginManager(String appEnvironment) {
		super(appEnvironment);
	}

	/*private final static JFishPluginManager instance = new DefaultPluginManager();
	
	public static JFishPluginManager getInstance() {
		return instance;
	}*/

//	private JFishList<JFishPluginMeta> pluginMetas;
//	private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
	
//	private ApplicationContext applicationContext;
//	private final ContextPluginManager contextPluginManager;
	


	
	/*@Override
	public void afterPropertiesSet() throws Exception {
	}*/

	/*@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}*/

	public JFishMvcEventBus getMvcEventBus() {
		return mvcEventBus;
	}


	public PluginNameParser getPluginNameParser() {
		return pluginNameParser;
	}
	
	public void setPluginNameParser(PluginNameParser pluginNameParser) {
		this.pluginNameParser = pluginNameParser;
	}

	
	@Override
	protected PluginInfo buildPluginInfo(JFishProperties prop){
		PluginInfo info = new JFishPluginInfo();
		info.init(prop);
		return info;
	}
	
	protected void doInitSinglePlugin(PluginInfo plugin){
		JFishPluginMeta meta = createPluginMeta(plugin);
		plugin.setInitialized();
		pluginMetas.add(meta);
		logger.info("init plugin["+plugin+"]..." );

		meta.getContextPlugin().init(meta, getAppEnvironment());
		if(meta.getJFishPlugin()!=null)
			meta.getJFishPlugin().init(meta);
	}
	
	@Override
	protected JFishPluginMeta createPluginMeta(PluginInfo pluginInfo) {
		JFishPluginInfo jfishPluginInfo = (JFishPluginInfo) pluginInfo;
		
		if(StringUtils.isBlank(jfishPluginInfo.getPluginClass()) && StringUtils.isBlank(jfishPluginInfo.getWebPluginClass())){
			throw new BaseException("both pluginClass and webPluginClass ie empty in plugin : " + pluginInfo.getName());
		}
		
		ContextPlugin contextPlugin = null;
		if(StringUtils.isBlank(jfishPluginInfo.getPluginClass())){
			contextPlugin = new EmptyContextPlugin();
		}else{
			contextPlugin = ReflectUtils.newInstance(pluginInfo.getPluginClass());
		}
		
		JFishPlugin jfishPlugin = null;
		//
		if(StringUtils.isBlank(jfishPluginInfo.getWebPluginClass())){
//			jfishPlugin = JFishPlugin.EMPTY_JFISH_PLUGIN;
			jfishPlugin = new EmptyJFishPluginAdapter();
		}else{
			jfishPlugin = ReflectUtils.newInstance(jfishPluginInfo.getWebPluginClass());
		}
		return new DefaultJFishPluginMeta(jfishPlugin, contextPlugin, jfishPluginInfo, pluginNameParser);
	}

	@Override
	public void onInitWebApplicationContext(final WebApplicationContext appContext){
		getMvcEventBus().postWebApplicationStartupEvent(appContext);
		/*pluginMetas.each(new NoIndexIt<JFishPluginMeta>(){

			@Override
			public void doIt(JFishPluginMeta meta) {
				JFishPluginUtils.getJFishPlugin(meta).onStartWebAppConext(appContext);
			}
			
		});*/
	}
	
	@Override
	public void destroy(final WebApplicationContext webApplicationContext){
		getMvcEventBus().postWebApplicationStopEvent(webApplicationContext);
		/*pluginMetas.each(new NoIndexIt<JFishPluginMeta>(){

			@Override
			public void doIt(JFishPluginMeta meta) {
				logger.info("stop plugin["+meta.getPluginInfo().getName()+"]..." );
				JFishPluginUtils.getJFishPlugin(meta).onStopWebAppConext();
			}
			
		});*/
		this.pluginMetas.clear();
	}
	

	/*@Override
	public void onCreateCacheManager(final CacheManager cacheManager, final boolean isJfishCache) {
		// TODO Auto-generated method stub
		getPluginMetas().each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) {
				e.getJfishPlugin().getJFishContextConfigurerListener().onCreateCacheManager(cacheManager, isJfishCache);
			}
			
		});
	}*/

	@Override
	public JFishList<JFishPluginMeta> getPluginMetas() {
		return pluginMetas;
	}
	
	@Override
	public JFishPluginMeta getJFishPluginMetaOf(Class<?> objClass){
		if(LangUtils.isEmpty(pluginMetas))
			return null;
		JFishList<JFishPluginMeta> list = JFishList.newList(5);
		for(JFishPluginMeta plugin : pluginMetas){
			if(plugin.isClassOfThisPlugin(objClass))
				list.add(plugin);
		}
		if(list.size()==0){
			return null;
		}else if(list.size()==1){
			return list.get(0);
		}
		else{
			list.sort(new Comparator<JFishPluginMeta>() {
	
				@Override
				public int compare(JFishPluginMeta o1, JFishPluginMeta o2) {
					return o2.getRootClass().getPackage().getName().length()-o1.getRootClass().getPackage().getName().length();
				}
				
			});
			return list.get(0);
		}
	}
	

	public JFishPlugin getJFishPlugin(String name){
		return JFishPluginUtils.getJFishPlugin(getJFishPluginMeta(name));
	}

	public JFishPluginMeta getJFishPluginMeta(String name){
		for(JFishPluginMeta plugin : pluginMetas){
			if(plugin.getPluginInfo().getName().equals(name))
				return plugin;
		}
		throw new JFishException("can not find the jfish plugin: " + name);
	}
	
	/*@Override
	public void registerPluginMvcContextClasses(final List<Class<?>> annoClasses){
		
		getPluginMetas().each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta element) {
				JFishPluginUtils.getJFishPlugin(element).onMvcContextClasses(annoClasses);
			}
			
		});
		
	}*/
	
	/*@Override
	public void registerPluginJFishContextClasses(final List<Class<?>> annoClasses){
		
		getPluginMetas().each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta element) {
				element.getContextPlugin().onJFishContextClasses(annoClasses);
			}
			
		});
		
	}*/

	/*@Override
	public void onMvcBuildFreeMarkerConfigurer(final JFishFreeMarkerConfigurer config, final boolean hasBuilt){
		getPluginMetas().each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) {
				String rspath = e.getWebResourceMeta().getTemplatePath();
				rspath = FileUtils.closePath(rspath);
				templatePaths.add(rspath);
				logger.info("add plugin["+e+"]resource path : " + rspath);
				
				e.getJfishPlugin().getJFishMvcConfigurerListener().onFreeMarkerConfigurer(freeMarker, templatePaths, setting);
			}
			
		});

		getPluginMetas().each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) {
				if(hasBuilt){
					String rspath = e.getWebResourceMeta().getTemplatePath();
					rspath = StringUtils.appendEndWith(rspath, "/");
					config.addPluginTemplateLoader(e.getPluginInfo().getName(), rspath);
					
					logger.info("add plugin["+e.getPluginInfo().getName()+"]resource path : " + rspath);
				}
				
				JFishPluginUtils.getJFishPlugin(e).getJFishMvcConfigurerListener().onMvcBuildFreeMarkerConfigurer(config, hasBuilt);
			}
			
		});
	}*/

	
	/*@Override
	public void onMvcPropertyEditorRegistrars(final List<PropertyEditorRegistrar> propertyEditorRegistrars) {
		this.pluginMetas.each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) {
				JFishPluginUtils.getJFishPlugin(e).getJFishMvcConfigurerListener().onMvcPropertyEditorRegistrars(propertyEditorRegistrars);
			}
			
		});
	}

	@Override
	public void onMvcInitContext(final JFishMvcApplicationContext applicationContext, final JFishMvcConfig mvcConfig) {
		this.registerMvcResourcesOfPlugins(applicationContext);
		
		this.pluginMetas.each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) {
				JFishPluginUtils.getJFishPlugin(e).getJFishMvcConfigurerListener().onMvcInitContext(applicationContext, mvcConfig);
			}
			
		});
	}*/

	/*@Override
	public void onRegisterArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
		this.pluginMetas.each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) {
				JFishPluginUtils.getJFishPlugin(e).getJFishMvcConfigurerListener().onRegisterArgumentResolvers(argumentResolvers);
			}
			
		});
	}
	
	@Subscribe
	public void listeningMvcConfigurer(ArgumentResolverEvent event){
		a
	}*/

	/*private void registerMvcResourcesOfPlugins(final JFishMvcApplicationContext applicationContext){
		final Map<String, String> urlMap = new ManagedMap<String, String>();

		getPluginMetas().each(new It<JFishPluginMeta>() {

			@Override
			public boolean doIt(JFishPluginMeta element, int index) {
				if(!JFishPluginUtils.getJFishPlugin(element).registerMvcResources()){
					return true;
				}
				final String locations = element.getWebResourceMeta().getStaticResourcePath();
				//TODO cacheSeconds
				final String rsBeanName = ResourceHttpRequestHandler.class.getSimpleName()+"#jfishPlugin#"+element.getPluginInfo().getName();
				applicationContext.registerAndGetBean(rsBeanName, ResourceHttpRequestHandler.class, "locations", locations);
				//  pluginPath/static/** 
				urlMap.put(element.getPluginInfo().getContextPath()+"/static/**", rsBeanName);
				logger.info("mapped plugin["+element.getPluginInfo().getName()+"] resource: [" + locations + "] to [" + element.getPluginInfo().getContextPath()+"/static/**]");
				
				return true;
			}
			
		});
		
		if(logger.isInfoEnabled()){
			logger.info("registerMvcResourcesOfPlugins: " + urlMap);
		}
		applicationContext.registerAndGetBean(SimpleUrlHandlerMapping.class.getSimpleName()+"#jfishPlugin", SimpleUrlHandlerMapping.class, "urlMap", urlMap, "order", Ordered.LOWEST_PRECEDENCE - 1);
	}*/
	
	@Override
	public List<JFishPlugin> getJFishPlugins() {
		return JFishList.wrap(getPluginMetas()).map(new MapIt<JFishPluginMeta, JFishPlugin>() {

			@Override
			public JFishPlugin map(JFishPluginMeta element, int index) {
				return JFishPluginUtils.getJFishPlugin(element);
			}
			
		});
	}
}
