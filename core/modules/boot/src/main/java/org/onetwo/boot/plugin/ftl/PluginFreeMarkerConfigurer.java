package org.onetwo.boot.plugin.ftl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.ftl.FtlUtils;
import org.onetwo.common.web.view.ftl.AbstractDirective;
import org.onetwo.common.web.view.ftl.DefineDirective;
import org.onetwo.common.web.view.ftl.ExtendsDirective;
import org.onetwo.common.web.view.ftl.OverrideDirective;
import org.onetwo.common.web.view.ftl.ProfileDirective;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class PluginFreeMarkerConfigurer extends FreeMarkerConfigurer {

	public static final BeansWrapper INSTANCE = FtlUtils.BEAN_WRAPPER;
	private Map<String, Object> fishFreemarkerVariables = new HashMap<String, Object>();
//	private ResourceLoader classPathResourceLoader = new DefaultResourceLoader();
	
	public PluginFreeMarkerConfigurer addDirective(AbstractDirective directive){
		return addDirective(directive, false);
	}

	public PluginFreeMarkerConfigurer addDirective(AbstractDirective directive, boolean override){
		if(!override && this.fishFreemarkerVariables.containsKey(directive.getDirectiveName()))
			throw new BaseException("the freemarker directive name has exist : " + directive.getDirectiveName());
		this.fishFreemarkerVariables.put(directive.getDirectiveName(), directive);
		return this;
	}

	public void afterPropertiesSet() throws IOException, TemplateException {
		if(this.fishFreemarkerVariables==null)
			this.fishFreemarkerVariables = new HashMap<String, Object>();

		this.fishFreemarkerVariables.put(ExtendsDirective.DIRECTIVE_NAME, new ExtendsDirective());
		this.fishFreemarkerVariables.put(DefineDirective.DIRECTIVE_NAME, new DefineDirective());
		this.fishFreemarkerVariables.put(OverrideDirective.DIRECTIVE_NAME, new OverrideDirective());
		this.fishFreemarkerVariables.put(ProfileDirective.DIRECTIVE_NAME, new ProfileDirective());

		/*this.fishFreemarkerVariables.put(DataGridDirective.DIRECTIVE_NAME, new DataGridDirective());
		this.fishFreemarkerVariables.put(DataRowDirective.DIRECTIVE_NAME, new DataRowDirective());
		this.fishFreemarkerVariables.put(DataFieldDirective.DIRECTIVE_NAME, new DataFieldDirective());
		this.fishFreemarkerVariables.put(DataComponentDirective.DIRECTIVE_NAME, new DataComponentDirective());*/

		super.setFreemarkerVariables(this.fishFreemarkerVariables);
		super.afterPropertiesSet();

	}
	

	public void setFreemarkerVariables(Map<String, Object> variables) {
		this.fishFreemarkerVariables = variables;
	}
	
	protected void postProcessConfiguration(Configuration config) throws IOException, TemplateException {
		config.setObjectWrapper(INSTANCE);
		config.setSetting("classic_compatible", "true");
		//默认不格式化数字
		config.setNumberFormat("#");
	}

	protected Map<String, Object> getFreemarkerVariables() {
		return fishFreemarkerVariables;
	}
	
	/*@Override
	protected TemplateLoader getTemplateLoaderForPath(String templateLoaderPath) {
		if (isPreferFileSystemAccess()) {
			// Try to load via the file system, fall back to SpringTemplateLoader
			// (for hot detection of template changes, if possible).
			try {
				Resource path = getResourceLoader().getResource(templateLoaderPath);
				File file = path.getFile();  // will fail if not resolvable in the file system
				if (logger.isDebugEnabled()) {
					logger.debug(
							"Template loader path [" + path + "] resolved to file path [" + file.getAbsolutePath() + "]");
				}
				return new FileTemplateLoader(file);
			}
			catch (IOException ex) {
				if (logger.isDebugEnabled()) {
					logger.debug("Cannot resolve template loader path [" + templateLoaderPath +
							"] to [java.io.File]: using SpringTemplateLoader as fallback", ex);
				}
				return new SpringTemplateLoader(classPathResourceLoader, templateLoaderPath);
			}
		}
		else {
			// Always load via SpringTemplateLoader (without hot detection of template changes).
			logger.debug("File system access not preferred: using SpringTemplateLoader");
			return new SpringTemplateLoader(classPathResourceLoader, templateLoaderPath);
		}
	}*/

	/*public void addPluginTemplateLoader(String name, String templateLoaderPath) {
		this.checkFreezing("addPluginTemplateLoader");
		final TemplateLoader loader = getConfiguration().getTemplateLoader();
		final JFishPluginTemplateLoader jfishLoader = JFishPluginTemplateLoader.class.isInstance(loader)?(JFishPluginTemplateLoader)loader:null;
		if(jfishLoader==null){
//			logger.error("not found JFishPluginTemplateLoader. ");
			throw new BaseException("not found JFishPluginTemplateLoader: " + name);
		}
		if(jfishLoader.containsPluginTemplateLoader(name)){
			throw new BaseException("the jfish has contains the plugin template loader: " + name);
		}
		TemplateLoader pluginLoader = getTemplateLoaderForPath(templateLoaderPath);
		jfishLoader.addPluginTemplateLoader(name, pluginLoader);
	}*/

	/*protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {
//		return super.getAggregateTemplateLoader(templateLoaders);
		int loaderCount = templateLoaders.size();
		switch (loaderCount) {
			case 0:
				logger.info("No FreeMarker TemplateLoaders specified");
				return null;
			case 1:
				return templateLoaders.get(0);
			default:
				TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[loaderCount]);
				StatefulTemplateLoader loader = new JFishPluginTemplateLoader(loaders, jfishPluginManager);
//				if(this.pluginManager!=null) this.pluginManager.onTemplateLoader();
				return loader;
		}
	}*/
}
