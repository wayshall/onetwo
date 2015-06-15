package org.onetwo.common.spring.ftl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.fish.plugin.JFishWebMvcPluginManager;
import org.onetwo.common.fish.plugin.JFishPluginTemplateLoader;
import org.onetwo.common.ftl.directive.AbstractDirective;
import org.onetwo.common.ftl.directive.DefineDirective;
import org.onetwo.common.ftl.directive.ExtendsDirective;
import org.onetwo.common.ftl.directive.OverrideDirective;
import org.onetwo.common.ftl.directive.ProfileDirective;
import org.onetwo.common.spring.web.mvc.config.event.JFishMvcEventBus;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.StatefulTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class JFishFreeMarkerConfigurer extends FreeMarkerConfigurer {

	public static final BeansWrapper INSTANCE = FtlUtils.BEAN_WRAPPER;
	private Map<String, Object> fishFreemarkerVariables = new HashMap<String, Object>();
	private JFishMvcEventBus jfishMvcEventBus;
	private boolean freezing;
	
	private JFishWebMvcPluginManager jfishPluginManager;


	public JFishFreeMarkerConfigurer(JFishMvcEventBus listenerManager) {
		super();
		this.jfishMvcEventBus = listenerManager;
	}

	public void setJfishPluginManager(JFishWebMvcPluginManager jfishPluginManager) {
		this.jfishPluginManager = jfishPluginManager;
	}

	public JFishFreeMarkerConfigurer addDirective(AbstractDirective directive){
		return addDirective(directive, false);
	}

	public JFishFreeMarkerConfigurer addDirective(AbstractDirective directive, boolean override){
		this.checkFreezing("addDirective");
		if(!override && this.fishFreemarkerVariables.containsKey(directive.getDirectiveName()))
			throw new JFishException("the freemarker directive name has exist : " + directive.getDirectiveName());
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

		this.jfishMvcEventBus.postFreeMarkerConfigurer(jfishPluginManager, this, false);
		
		super.setFreemarkerVariables(this.fishFreemarkerVariables);
		super.afterPropertiesSet();

		this.jfishMvcEventBus.postFreeMarkerConfigurer(jfishPluginManager, this, true);
		
		this.freezing = true;
	}
	
	private void checkFreezing(String op){
		if(this.freezing){
			throw new JFishException("the object is freezing on this operation: " + op);
		}
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

	/*public void addTemplateLoader(String templateLoaderPath) {
		this.checkFreezing("addTemplateLoader");
		final TemplateLoader loader = getConfiguration().getTemplateLoader();
		final JFishPluginTemplateLoader jfishLoader = JFishPluginTemplateLoader.class.isInstance(loader)?(JFishPluginTemplateLoader)loader:null;
		if(jfishLoader==null){
			logger.error("not found JFishPluginTemplateLoader. ");
		}
		TemplateLoader pluginLoader = getTemplateLoaderForPath(templateLoaderPath);
		jfishLoader.addTemplateLoader(pluginLoader);
	}*/

	public void addPluginTemplateLoader(String name, String templateLoaderPath) {
		this.checkFreezing("addPluginTemplateLoader");
		final TemplateLoader loader = getConfiguration().getTemplateLoader();
		final JFishPluginTemplateLoader jfishLoader = JFishPluginTemplateLoader.class.isInstance(loader)?(JFishPluginTemplateLoader)loader:null;
		if(jfishLoader==null){
//			logger.error("not found JFishPluginTemplateLoader. ");
			throw new JFishException("not found JFishPluginTemplateLoader: " + name);
		}
		if(jfishLoader.containsPluginTemplateLoader(name)){
			throw new JFishException("the jfish has contains the plugin template loader: " + name);
		}
		TemplateLoader pluginLoader = getTemplateLoaderForPath(templateLoaderPath);
		jfishLoader.addPluginTemplateLoader(name, pluginLoader);
	}

	protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {
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
	}
}
