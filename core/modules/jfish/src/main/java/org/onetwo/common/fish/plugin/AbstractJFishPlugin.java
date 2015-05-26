package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;
import org.slf4j.Logger;

public abstract class AbstractJFishPlugin<T> implements JFishPlugin{

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private JFishPluginMeta pluginMeta;
	private PluginConfig pluginConfig = new DefaultPluginConfig();

	/*@Override
	public void onStartWebAppConext(WebApplicationContext appContext) {
	}*/
	

	@Override
	public void init(JFishPluginMeta pluginMeta) {
		this.pluginMeta = pluginMeta;
		this.setPluginInstance((T)this);
	}

	abstract public void setPluginInstance(T plugin);


	@Override
	public JFishMvcPluginListener getJFishMvcConfigurerListener() {
		return new JFishMvcConfigurerListenerAdapter(this);
	}

	/*@Override
	public void onStopWebAppConext() {
		this.pluginMeta = null;
	}*/


	/*@Override
	public JFishContextConfigurerListener getJFishContextConfigurerListener() {
		return new JFishContextConfigurerListenerAdapter();
	}*/

	/****
	 * @see JFishMvcPluginListener
	 * @param annoClasses
	 */
	@Deprecated
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
	}


	@Override
	public boolean registerMvcResources() {
		return false;
	}


	@Override
	public PluginConfig getPluginConfig() {
		return pluginConfig;
	}


	public JFishPluginMeta getPluginMeta() {
		return pluginMeta;
	}


	@Override
	public boolean isEmptyPlugin() {
		return false;
	}

	public String getPluginTemplatePath(String template) {
		return getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

}
