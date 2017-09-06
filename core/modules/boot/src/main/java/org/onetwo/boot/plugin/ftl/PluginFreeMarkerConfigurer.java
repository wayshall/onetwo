package org.onetwo.boot.plugin.ftl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.core.WebPlugin;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.ftl.FtlUtils;
import org.onetwo.common.web.view.ftl.AbstractDirective;
import org.onetwo.common.web.view.ftl.DefineDirective;
import org.onetwo.common.web.view.ftl.ExtendsDirective;
import org.onetwo.common.web.view.ftl.OverrideDirective;
import org.onetwo.common.web.view.ftl.ProfileDirective;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class PluginFreeMarkerConfigurer extends FreeMarkerConfigurer {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	public static final String DEFAULT_PLUGIN_FTL_LOCATION = "classpath:META-INF/resources/webftls/";
	
	public static final BeansWrapper INSTANCE = FtlUtils.BEAN_WRAPPER;
	private Map<String, Object> freemarkerVariablesHoder;
	private Map<String, Object> extFreemarkerVariables = new HashMap<String, Object>();
//	private ResourceLoader classPathResourceLoader = new DefaultResourceLoader();
//	private String[] templateLoaderPathsHolder;
	
	@Autowired
	private PluginManager pluginManager;
	@Autowired
	private ApplicationContext applicationContext;
	
	public PluginFreeMarkerConfigurer addDirective(AbstractDirective directive){
		return addDirective(directive, false);
	}

	public PluginFreeMarkerConfigurer addDirective(AbstractDirective directive, boolean override){
		if(!override && this.freemarkerVariablesHoder.containsKey(directive.getDirectiveName()))
			throw new BaseException("the freemarker directive name has exist : " + directive.getDirectiveName());
		this.freemarkerVariablesHoder.put(directive.getDirectiveName(), directive);
		return this;
	}

	public void afterPropertiesSet() throws IOException, TemplateException {
		if(this.freemarkerVariablesHoder==null)
			this.freemarkerVariablesHoder = new HashMap<String, Object>();

		this.freemarkerVariablesHoder.put(ExtendsDirective.DIRECTIVE_NAME, new ExtendsDirective()/*{
			protected String getActaulFile(Map params, String file){
				String filePath = super.getActaulFile(params, file);
				String plugin = DirectivesUtils.getParameterByString(params, "plugin", "");
				if(StringUtils.isNotBlank(plugin)){
					filePath = pluginManager.getPluginTemplateBasePath(plugin)+filePath;
				}
				return filePath;
			}
		}*/);
		this.freemarkerVariablesHoder.put(DefineDirective.DIRECTIVE_NAME, new DefineDirective());
		this.freemarkerVariablesHoder.put(OverrideDirective.DIRECTIVE_NAME, new OverrideDirective());
		this.freemarkerVariablesHoder.put(ProfileDirective.DIRECTIVE_NAME, new ProfileDirective());

		this.freemarkerVariablesHoder.putAll(extFreemarkerVariables);
		/*this.fishFreemarkerVariables.put(DataGridDirective.DIRECTIVE_NAME, new DataGridDirective());
		this.fishFreemarkerVariables.put(DataRowDirective.DIRECTIVE_NAME, new DataRowDirective());
		this.fishFreemarkerVariables.put(DataFieldDirective.DIRECTIVE_NAME, new DataFieldDirective());
		this.fishFreemarkerVariables.put(DataComponentDirective.DIRECTIVE_NAME, new DataComponentDirective());*/

		super.setFreemarkerVariables(this.freemarkerVariablesHoder);
		super.afterPropertiesSet();

	}
	
	@Override
	protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {
		super.postProcessTemplateLoaders(templateLoaders);
//		pluginManager.getPlugins().forEach(plugin->findPluginTemplateLoader(templateLoaders, plugin));
	}
	
	protected TemplateLoader findPluginTemplateLoader(WebPlugin plugin){
		String templateLoaderPath = DEFAULT_PLUGIN_FTL_LOCATION + plugin.getPluginMeta().getName();
		TemplateLoader loader = getTemplateLoaderForPath(templateLoaderPath);
		if(logger.isInfoEnabled()){
			logger.info("add template loader from path : " + templateLoaderPath);
		}
		return loader;
	}

	@Override
	protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {
//		return super.getAggregateTemplateLoader(templateLoaders);
		/*int loaderCount = templateLoaders.size();
		switch (loaderCount) {
			case 0:
				logger.info("No FreeMarker TemplateLoaders specified");
				return null;
			case 1:
				return templateLoaders.get(0);
			default:
				TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[loaderCount]);
				PluginTemplateLoader pluginLoader = new PluginTemplateLoader(loaders, pluginManager);
//				if(this.pluginManager!=null) this.pluginManager.onTemplateLoader();
				pluginManager.getPlugins().forEach(plugin->{
					pluginLoader.putPluginTemplateLoader(plugin, findPluginTemplateLoader(plugin));
				});
				return pluginLoader;
		}*/
		
		TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]);
		PluginTemplateLoader pluginLoader = new PluginTemplateLoader(loaders, pluginManager);
//		if(this.pluginManager!=null) this.pluginManager.onTemplateLoader();
		pluginManager.getPlugins().forEach(plugin->{
			pluginLoader.putPluginTemplateLoader(plugin, findPluginTemplateLoader(plugin));
		});
		return pluginLoader;
	}
	
	@Override
	protected TemplateLoader getTemplateLoaderForPath(String templateLoaderPath) {
		if(logger.isInfoEnabled()){
			logger.info("getTemplateLoaderForPathh : " + templateLoaderPath);
		}
		if (isPreferFileSystemAccess()) {
			// Try to load via the file system, fall back to SpringTemplateLoader
			// (for hot detection of template changes, if possible).
			try {
				Resource path = getResourceLoader().getResource(templateLoaderPath);
				File file = path.getFile();  // will fail if not resolvable in the file system
				if (logger.isInfoEnabled()) {
					logger.info(
							"Template loader path [" + path + "] resolved to file path [" + file.getAbsolutePath() + "]");
				}
				return new FileTemplateLoader(file);
			}
			catch (IOException ex) {
				if (logger.isInfoEnabled()) {
					logger.info("Cannot resolve template loader path [" + templateLoaderPath +
							"] to [java.io.File]: using SpringTemplateLoader as fallback", ex);
				}
				return new SpringTemplateLoader(getResourceLoader(), templateLoaderPath);
			}
		}
		else {
			// Always load via SpringTemplateLoader (without hot detection of template changes).
			logger.info("File system access not preferred: using SpringTemplateLoader");
			return new SpringTemplateLoader(getResourceLoader(), templateLoaderPath);
		}
	}
	

	public void setFreemarkerVariables(Map<String, Object> variables) {
		this.freemarkerVariablesHoder = variables;
	}
	
	public void setFreemarkerVariable(String name, Object value){
		this.extFreemarkerVariables.put(name, value);
	}
	
	protected void postProcessConfiguration(Configuration config) throws IOException, TemplateException {
		config.setObjectWrapper(INSTANCE);
		config.setSetting("classic_compatible", "true");
		//默认不格式化数字
		config.setNumberFormat("#");
	}

	protected Map<String, Object> getFreemarkerVariables() {
		return freemarkerVariablesHoder;
	}
	
}
