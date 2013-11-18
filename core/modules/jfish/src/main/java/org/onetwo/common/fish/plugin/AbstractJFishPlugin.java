package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.plugin.AbstractContextPlugin;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractJFishPlugin<T extends JFishPlugin> extends AbstractContextPlugin<T, JFishPluginMeta> implements JFishPlugin{
	

	@Override
	public void onStartWebAppConext(WebApplicationContext appContext) {
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


	@Override
	public PluginConfig getPluginConfig() {
		return new DefaultPluginConfig();
	}


}
