package org.onetwo.common.fish.plugin;

import java.util.List;
import java.util.Map;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.spring.plugin.PluginInfo;
import org.onetwo.common.spring.plugin.SpringContextPluginManager;
import org.onetwo.common.spring.web.mvc.config.JFishMvcApplicationContext;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfig;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.It;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.slf4j.Logger;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.core.Ordered;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

public class DefaultPluginManager extends SpringContextPluginManager<JFishPluginMeta> implements JFishPluginManager, JFishMvcConfigurerListener {
	

	private PluginNameParser pluginNameParser = new PluginNameParser("[", "]");
	
	/*private final static JFishPluginManager instance = new DefaultPluginManager();
	
	public static JFishPluginManager getInstance() {
		return instance;
	}*/

	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
//	private JFishList<JFishPluginMeta> pluginMetas;
//	private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
	
//	private ApplicationContext applicationContext;

	public DefaultPluginManager(){
		super(BaseSiteConfig.getInstance().getAppEnvironment());
	}
	
	/*@Override
	public void afterPropertiesSet() throws Exception {
	}*/

	/*@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}*/

	public PluginNameParser getPluginNameParser() {
		return pluginNameParser;
	}
	
	public void setPluginNameParser(PluginNameParser pluginNameParser) {
		this.pluginNameParser = pluginNameParser;
	}
	
	
	@Override
	protected JFishPluginMeta createPluginMeta(PluginInfo plugin) {
		return new DefaultJFishPluginMeta(plugin, pluginNameParser);
	}

	@Override
	public void onInitWebApplicationContext(final WebApplicationContext appContext){
		pluginMetas.each(new NoIndexIt<JFishPluginMeta>(){

			@Override
			public void doIt(JFishPluginMeta meta) {
				JFishPluginUtils.getJFishPlugin(meta).onStartWebAppConext(appContext);
			}
			
		});
	}
	
	@Override
	public void destroy(){
		pluginMetas.each(new NoIndexIt<JFishPluginMeta>(){

			@Override
			public void doIt(JFishPluginMeta meta) {
				logger.info("stop plugin["+meta.getJfishPlugin()+"]..." );
				JFishPluginUtils.getJFishPlugin(meta).onStopWebAppConext();
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
		return JFishPluginUtils.getJFishPlugin(getJFishPluginMeta(name));
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
				JFishPluginUtils.getJFishPlugin(element).onMvcContextClasses(annoClasses);
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
				
				JFishPluginUtils.getJFishPlugin(e).getJFishMvcConfigurerListener().onMvcBuildFreeMarkerConfigurer(config, hasBuilt);
			}
			
		});
	}

	
	@Override
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
	}

	private void registerMvcResourcesOfPlugins(final JFishMvcApplicationContext applicationContext){
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
	}
	
}
