package org.onetwo.common.fish.plugin;

import java.util.List;

import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.springframework.web.context.WebApplicationContext;


public class EmptyJFishPluginAdapter extends AbstractJFishPlugin<Object> {
	
//	private static JFishPluginAdapter instance = new JFishPluginAdapter();
	
	private EmptyJFishMvcConfigurerListener emptyJFishMvcConfigurerListener = new EmptyJFishMvcConfigurerListener();
	
	
	EmptyJFishPluginAdapter() {
		super();
	}

	@Override
	public void setPluginInstance(Object plugin) {
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
		return emptyJFishMvcConfigurerListener;
	}

	@Override
	public boolean registerMvcResources() {
		return false;
	}

	@Override
	public boolean isEmptyPlugin() {
		return true;
	}

	
}
