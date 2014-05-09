package org.onetwo.common.spring.plugin;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.onetwo.common.utils.propconf.PropConfig;
import org.onetwo.common.utils.propconf.PropUtils;
import org.onetwo.common.utils.propconf.VariablePropConifg;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

 public class SpringContextPluginManager<T extends ContextPluginMeta> implements ContextPluginManager {

	public static final String PLUGIN_PATH = "classpath*:META-INF/jfish-plugin.properties";
	
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	protected JFishList<T> pluginMetas;
	private String pluginPath = PLUGIN_PATH;
	private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
	private final String appEnvironment;
	private final Map<String, PluginInfo> pluginConfs = new LinkedHashMap<String, PluginInfo>();
	

	public SpringContextPluginManager(String appEnvironment) {
		this(PLUGIN_PATH, appEnvironment);
	}
	
	public SpringContextPluginManager(String pluginPath, String appEnvironment) {
		super();
		this.pluginPath = pluginPath;
		this.appEnvironment = appEnvironment;
	}

	public String getAppEnvironment() {
		return appEnvironment;
	}

	/****
	 * scan plugins on webapp application start 
	 * {@linkplain org.onetwo.common.fish.web.JFishWebApplicationContext JFishWebApplicationContext},
	 * it common start by web context listener.
	 */
	/********
	 * on initWebApplicationContext
	 * 扫描插件，并初始化
	 */
	@Override
	public void scanPlugins(){
		JFishList<Resource> pluginFiles = null;
		try {
			Resource[] resources = patternResolver.getResources(pluginPath);
			pluginFiles = new JFishList<Resource>(resources.length);
			pluginFiles.addArray(resources);
		} catch (IOException e1) {
			throw new ContextPluginException("scan plugin error: " + e1.getMessage(), e1);
		}
		
		pluginFiles.each(new NoIndexIt<Resource>(){

			@Override
			public void doIt(Resource pluginFile) throws Exception {
				VariablePropConifg vconfig = new VariablePropConifg();
				PropUtils.loadProperties(pluginFile.getInputStream(), vconfig);
				PropConfig prop = new PropConfig(vconfig, true);
				vconfig.setPropConfig(prop);
				
				PluginInfo plugin = buildPluginInfo(prop);
				logger.info("found plugin["+plugin+"]..." );
				logger.info("load plugin config :"+pluginFile.toString());
				pluginConfs.put(plugin.getName(), plugin);
			}
			
		});

		pluginMetas = new JFishList<T>(pluginFiles.size());
		this.initPlugins();
	}
	
	protected void initPlugins(){
		logger.info("================ jfish plugins ================");
		JFishList.wrap(pluginConfs.values()).each(new NoIndexIt<PluginInfo>(){

			@Override
			public void doIt(PluginInfo plugin) throws Exception {
				initPlugin(pluginConfs, plugin, null);
			}
			
		});
		logger.info("================ jfish plugins ================");
	}
	
	protected PluginInfo buildPluginInfo(PropConfig prop){
		PluginInfo info = new PluginInfo();
		info.init(prop);
		return info;
	}
	
/*	public static interface PluginInitCallback {
		public void doInit(PluginInfo pluginInfo);
	};*/
	
	private void initPlugin(Map<String, PluginInfo> pluginConfs, PluginInfo plugin, PluginInfo childPlugin){
		String scope = getAppEnvironment();
		if(!plugin.applyOnScope(scope)){
			if(childPlugin!=null){
				throw new BaseException("the plugin["+plugin.getName()+"] that child plugin["+childPlugin.getName()+"] must dependency is not valid in this scope: " + scope);
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
					throw new ContextPluginException("start plugin["+plugin.getName()+"] error: can not find the dependency plugin[" + dependency+"], please install it!");
				initPlugin(pluginConfs, parentDependencyPlugin, plugin);
			}
		}
//		cb.doInit(plugin);
		this.doInitSinglePlugin(plugin);
		/*ContextPluginMeta meta = createPluginMeta(plugin);
		plugin.setInitialized();
		pluginMetas.add(meta);
		logger.info("init plugin["+plugin+"]..." );
		
		meta.getContextPlugin().init(meta);*/
	}
	
	protected void doInitSinglePlugin(PluginInfo plugin){
		T meta = createPluginMeta(plugin);
		plugin.setInitialized();
		pluginMetas.add(meta);
		logger.info("init plugin["+plugin+"]..." );
		
		meta.getContextPlugin().init(meta);
	}
	

//	abstract protected T createPluginMeta(PluginInfo plugin);
	protected T createPluginMeta(PluginInfo pluginInfo){
		ContextPlugin contextPlugin = ReflectUtils.newInstance(pluginInfo.getPluginClass());
		return (T)new DefaultContextPluginMeta(contextPlugin, pluginInfo);
	}

	
	@Override
	public void registerPluginJFishContextClasses(final List<Class<?>> annoClasses){
		
		getPluginMetas().each(new NoIndexIt<T>() {

			@Override
			protected void doIt(T element) {
				element.getContextPlugin().onJFishContextClasses(annoClasses);
			}
			
		});
		
	}
	//	@Override
	public JFishList<T> getPluginMetas() {
		return pluginMetas;
	}
	
	

}
