package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.springframework.web.context.WebApplicationContext;


public class JFishPluginAdapter implements JFishPlugin {
	
//	private static JFishPluginAdapter instance = new JFishPluginAdapter();
	
	
	JFishPluginAdapter() {
		super();
	}

	@Override
	public void onStartWebAppConext(WebApplicationContext appContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopWebAppConext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(JFishPluginMeta pluginMeta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JFishMvcConfigurerListener getJFishMvcConfigurerListener() {
		throw new UnsupportedOperationException();
	}

	@Override
	public JFishPluginMeta getPluginMeta() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean registerMvcResources() {
		return false;
	}

	@Override
	public PluginConfig getPluginConfig() {
		throw new UnsupportedOperationException();
	}

	
}
