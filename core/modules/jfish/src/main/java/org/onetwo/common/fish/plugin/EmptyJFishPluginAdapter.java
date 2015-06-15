package org.onetwo.common.fish.plugin;

import org.onetwo.common.spring.web.mvc.config.JFishMvcPluginListener;


public class EmptyJFishPluginAdapter extends AbstractJFishPlugin<Object> {
	
//	private static JFishPluginAdapter instance = new JFishPluginAdapter();
	
	private JFishMvcConfigurerListenerAdapter emptyJFishMvcConfigurerListener = new JFishMvcConfigurerListenerAdapter(this);
	
	
	EmptyJFishPluginAdapter() {
		super();
	}

	@Override
	public void setPluginInstance(Object plugin) {
	}


	@Override
	public void init(JFishWebMvcPluginMeta pluginMeta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JFishMvcPluginListener getJFishMvcConfigurerListener() {
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
