package org.onetwo.common.fish.plugin;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.spring.web.mvc.config.JFishMvcApplicationContext;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfig;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.It;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.onetwo.common.utils.propconf.PropConfig;
import org.onetwo.common.utils.propconf.PropUtils;
import org.onetwo.common.utils.propconf.VariablePropConifg;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.slf4j.Logger;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

public class DefaultPluginManager implements JFishPluginManager, JFishMvcConfigurerListener {
	
	public static final String PLUGIN_PATH = "classpath*:META-INF/jfish-plugin.properties";

	public static final PluginNameParser pluginNameParser = new PluginNameParser("[", "]");
	
	/*private final static JFishPluginManager instance = new DefaultPluginManager();
	
	public static JFishPluginManager getInstance() {
		return instance;
	}*/

	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	private JFishList<JFishPluginMeta> pluginMetas;
	private String pluginPath;
	private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
	
//	private ApplicationContext applicationContext;

	public DefaultPluginManager(){
		this(PLUGIN_PATH);
	}
	
	public DefaultPluginManager(String pluginPath){
		this.pluginPath = pluginPath;
	}
	
	/*@Override
	public void afterPropertiesSet() throws Exception {
	}*/

	/*@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}*/

	/********
	 * on initWebApplicationContext
	 */
	@Override
	public void scanPlugins(){
		JFishList<Resource> pluginFiles = null;
		try {
			Resource[] resources = patternResolver.getResources(pluginPath);
			pluginFiles = new JFishList<Resource>(resources.length);
			pluginFiles.addArray(resources);
		} catch (IOException e1) {
			throw new JFishException("scan plugin error: " + e1.getMessage(), e1);
		}
		
		final Map<String, PluginInfo> pluginConfs = new LinkedHashMap<String, PluginInfo>();
		pluginFiles.each(new NoIndexIt<Resource>(){

			@Override
			public void doIt(Resource pluginFile) throws Exception {
				VariablePropConifg vconfig = new VariablePropConifg();
				PropUtils.loadProperties(pluginFile.getInputStream(), vconfig);
				PropConfig prop = new PropConfig(vconfig, true);
				vconfig.setPropConfig(prop);
				
				PluginInfo plugin = PluginInfo.newFrom(prop);
				logger.info("found plugin["+plugin+"]..." );
				logger.info("load plugin config :"+pluginFile.toString());
				pluginConfs.put(plugin.getName(), plugin);
			}
			
		});

		pluginMetas = new JFishList<JFishPluginMeta>(pluginFiles.size());
		logger.info("================ jfish plugins ================");
		JFishList.wrap(pluginConfs.values()).each(new NoIndexIt<PluginInfo>(){

			@Override
			public void doIt(PluginInfo plugin) throws Exception {
				initPlugin(pluginConfs, plugin, null);
			}
			
		});
		logger.info("================ jfish plugins ================");
	}
	
	private void initPlugin(Map<String, PluginInfo> pluginConfs, PluginInfo plugin, PluginInfo childPlugin){
		String scope = BaseSiteConfig.getInstance().getAppEnvironment();
		if(!plugin.applyOnScope(scope)){
			if(childPlugin!=null){
				throw new JFishException("the plugin["+plugin.getName()+"] that child plugin["+childPlugin.getName()+"] must dependency is not valid in this scope: " + scope);
			}else{
				logger.info("the plugin["+plugin.getName()+"] is not valid in this scope: " + scope);
			}
			return ;
		}
		if(plugin.isInitialized()){
			return ;
		}
		if(!plugin.getDependency().isEmpty()){
			for(String dependency : plugin.getDependency()){
				PluginInfo parentDependencyPlugin = pluginConfs.get(dependency);
				if(parentDependencyPlugin==null)
					throw new JFishException("start plugin["+plugin.getName()+"] error: can not find the dependency plugin[" + dependency+"], please install it!");
				initPlugin(pluginConfs, parentDependencyPlugin, plugin);
			}
		}
		JFishPluginMeta meta = new DefaultJFishPluginMeta(plugin, pluginNameParser);
		plugin.setInitialized();
		pluginMetas.add(meta);
		logger.info("init plugin["+plugin+"]..." );
		
		meta.getJfishPlugin().init(meta);
	}
	
	@Override
	public void onInitWebApplicationContext(final WebApplicationContext appContext){
		pluginMetas.each(new NoIndexIt<JFishPluginMeta>(){

			@Override
			public void doIt(JFishPluginMeta meta) {
				meta.getJfishPlugin().onStartWebAppConext(appContext);
			}
			
		});
	}
	
	@Override
	public void destroy(){
		pluginMetas.each(new NoIndexIt<JFishPluginMeta>(){

			@Override
			public void doIt(JFishPluginMeta meta) {
				logger.info("stop plugin["+meta.getJfishPlugin()+"]..." );
				meta.getJfishPlugin().onStopWebAppConext();
			}
			
		});
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

	//	@Override
	public JFishList<JFishPluginMeta> getPluginMetas() {
		return pluginMetas;
	}
	
	@Override
	public JFishPluginMeta getJFishPluginMetaOf(Class<?> objClass){
		if(LangUtils.isEmpty(pluginMetas))
			return null;
		for(JFishPluginMeta plugin : pluginMetas){
			if(plugin.isClassOfThisPlugin(objClass))
				return plugin;
		}
		return null;
	}
	

	public JFishPlugin getJFishPlugin(String name){
		return getJFishPluginMeta(name).getJfishPlugin();
	}

	public JFishPluginMeta getJFishPluginMeta(String name){
		for(JFishPluginMeta plugin : pluginMetas){
			if(plugin.getPluginInfo().getName().equals(name))
				return plugin;
		}
		throw new JFishException("can not find the jfish plugin: " + name);
	}
	
	@Override
	public void registerPluginMvcContextClasses(final List<Class<?>> annoClasses){
		
		getPluginMetas().each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta element) {
				element.getJfishPlugin().onMvcContextClasses(annoClasses);
			}
			
		});
		
	}
	
	@Override
	public void registerPluginJFishContextClasses(final List<Class<?>> annoClasses){
		
		getPluginMetas().each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta element) {
				element.getJfishPlugin().onJFishContextClasses(annoClasses);
			}
			
		});
		
	}

	@Override
	public void onMvcBuildFreeMarkerConfigurer(final JFishFreeMarkerConfigurer config, final boolean hasBuilt){
		/*getPluginMetas().each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) {
				String rspath = e.getWebResourceMeta().getTemplatePath();
				rspath = FileUtils.closePath(rspath);
				templatePaths.add(rspath);
				logger.info("add plugin["+e+"]resource path : " + rspath);
				
				e.getJfishPlugin().getJFishMvcConfigurerListener().onFreeMarkerConfigurer(freeMarker, templatePaths, setting);
			}
			
		});*/

		getPluginMetas().each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) {
				if(hasBuilt){
					String rspath = e.getWebResourceMeta().getTemplatePath();
					rspath = StringUtils.appendEndWith(rspath, "/");
					config.addPluginTemplateLoader(e.getPluginInfo().getName(), rspath);
					
					logger.info("add plugin["+e+"]resource path : " + rspath);
				}
				
				e.getJfishPlugin().getJFishMvcConfigurerListener().onMvcBuildFreeMarkerConfigurer(config, hasBuilt);
			}
			
		});
	}

	
	@Override
	public void onMvcPropertyEditorRegistrars(final List<PropertyEditorRegistrar> propertyEditorRegistrars) {
		this.pluginMetas.each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) {
				e.getJfishPlugin().getJFishMvcConfigurerListener().onMvcPropertyEditorRegistrars(propertyEditorRegistrars);
			}
			
		});
	}

	@Override
	public void onMvcInitContext(final JFishMvcApplicationContext applicationContext, final JFishMvcConfig mvcConfig) {
		this.registerMvcResourcesOfPlugins(applicationContext);
		
		this.pluginMetas.each(new NoIndexIt<JFishPluginMeta>() {

			@Override
			protected void doIt(JFishPluginMeta e) {
				e.getJfishPlugin().getJFishMvcConfigurerListener().onMvcInitContext(applicationContext, mvcConfig);
			}
			
		});
	}

	private void registerMvcResourcesOfPlugins(final JFishMvcApplicationContext applicationContext){
		final Map<String, String> urlMap = new ManagedMap<String, String>();

		getPluginMetas().each(new It<JFishPluginMeta>() {

			@Override
			public boolean doIt(JFishPluginMeta element, int index) {
				if(!element.getJfishPlugin().registerMvcResources()){
					return true;
				}
				final String locations = element.getWebResourceMeta().getStaticResourcePath();
				//TODO cacheSeconds
				final String rsBeanName = ResourceHttpRequestHandler.class.getSimpleName()+"#jfishPlugin#"+element.getPluginInfo().getName();
				applicationContext.registerAndGetBean(rsBeanName, ResourceHttpRequestHandler.class, "locations", locations);
				//  pluginPath/static/** 
				urlMap.put(element.getPluginInfo().getContextPath()+"/static/**", rsBeanName);
				
				return true;
			}
			
		});
		
		if(logger.isInfoEnabled()){
			logger.info("registerMvcResourcesOfPlugins: " + urlMap);
		}
		applicationContext.registerAndGetBean(SimpleUrlHandlerMapping.class.getSimpleName()+"#jfishPlugin", SimpleUrlHandlerMapping.class, "urlMap", urlMap, "order", Ordered.LOWEST_PRECEDENCE - 1);
	}
	
}
