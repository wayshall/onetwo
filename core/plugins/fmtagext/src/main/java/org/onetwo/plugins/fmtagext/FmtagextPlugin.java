package org.onetwo.plugins.fmtagext;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.EmptyJFishMvcConfigurerListener;
import org.onetwo.common.spring.ftl.JFishFreeMarkerConfigurer;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.onetwo.plugins.fmtagext.directive.ExtEntityGridDirective;
import org.onetwo.plugins.fmtagext.directive.JFishEntryDirective;
import org.onetwo.plugins.fmtagext.directive.JFishFieldDirective;
import org.onetwo.plugins.fmtagext.ui.JFishUIDirective;


public class FmtagextPlugin extends AbstractJFishPlugin<FmtagextPlugin> {

	public static final String PLUGIN_NAME = "fmtagext";
	public static final String PLUGIN_PATH = "[fmtagext]";

	private static FmtagextPlugin instance;
	
	
	public static FmtagextPlugin getInstance() {
		return instance;
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(FmtagextContext.class);
	}
	
	public void setPluginInstance(FmtagextPlugin plugin){
		instance = plugin;
	}

	@Override
	public JFishMvcConfigurerListener getJFishMvcConfigurerListener() {
		return new EmptyJFishMvcConfigurerListener(){

			@Override
			public void onMvcBuildFreeMarkerConfigurer(final JFishFreeMarkerConfigurer config, final boolean hasBuilt){
				if(!hasBuilt){
					config.addDirective(new JFishEntryDirective());
					config.addDirective(new JFishFieldDirective());
					config.addDirective(new ExtEntityGridDirective(), true);
					
					config.addDirective(new JFishUIDirective());
				}
			}
		};
	}
}
