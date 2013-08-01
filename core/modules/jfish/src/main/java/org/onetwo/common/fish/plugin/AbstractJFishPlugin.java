package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractJFishPlugin<T extends JFishPlugin> implements JFishPlugin{

	private JFishPluginMeta pluginMeta;
	

	@SuppressWarnings("unchecked")
	@Override
	public void init(JFishPluginMeta pluginMeta) {
		this.pluginMeta = pluginMeta;
		this.setPluginInstance((T)this);
	}


	@Override
	public void onStartWebAppConext(WebApplicationContext appContext) {
	}
	
	abstract public void setPluginInstance(T plugin);


	public JFishPluginMeta getPluginMeta() {
		return pluginMeta;
	}

	@Override
	public JFishMvcConfigurerListener getJFishMvcConfigurerListener() {
		return new EmptyJFishMvcConfigurerListener();
	}

	@Override
	public void onStopWebAppConext() {
		this.pluginMeta = null;
	}


	/*@Override
	public JFishContextConfigurerListener getJFishContextConfigurerListener() {
		return new JFishContextConfigurerListenerAdapter();
	}*/


	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
	}


	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
	}


	@Override
	public boolean registerMvcResources() {
		return false;
	}


}
