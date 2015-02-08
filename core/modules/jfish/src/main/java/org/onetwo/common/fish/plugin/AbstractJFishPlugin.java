package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.slf4j.Logger;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractJFishPlugin<T> implements JFishPlugin{

	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private JFishPluginMeta pluginMeta;
	private PluginConfig pluginConfig = new DefaultPluginConfig();

	@Override
	public void onStartWebAppConext(WebApplicationContext appContext) {
	}
	

	@Override
	public void init(JFishPluginMeta pluginMeta) {
		this.pluginMeta = pluginMeta;
		this.setPluginInstance((T)this);
	}

	abstract public void setPluginInstance(T plugin);


	@Override
	public JFishMvcConfigurerListener getJFishMvcConfigurerListener() {
		return new JFishMvcConfigurerListenerAdapter(this);
	}

	@Override
	public void onStopWebAppConext() {
		this.pluginMeta = null;
	}


	/*@Override
	public JFishContextConfigurerListener getJFishContextConfigurerListener() {
		return new JFishContextConfigurerListenerAdapter();
	}*/


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


}
