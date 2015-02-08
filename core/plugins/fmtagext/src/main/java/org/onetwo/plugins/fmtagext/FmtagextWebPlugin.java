package org.onetwo.plugins.fmtagext;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.JFishMvcConfigurerListenerAdapter;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.onetwo.common.spring.web.mvc.config.event.FreeMarkerConfigurerBuildEvent;
import org.onetwo.plugins.fmtagext.directive.ExtEntityGridDirective;
import org.onetwo.plugins.fmtagext.directive.JFishEntryDirective;
import org.onetwo.plugins.fmtagext.directive.JFishFieldDirective;
import org.onetwo.plugins.fmtagext.ui.JFishUIDirective;


public class FmtagextWebPlugin extends AbstractJFishPlugin<FmtagextWebPlugin> {

	public static final String PLUGIN_NAME = "fmtagext";
	public static final String PLUGIN_PATH = "[fmtagext]";

	private static FmtagextWebPlugin instance;
	
	
	public static FmtagextWebPlugin getInstance() {
		return instance;
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(FmtagextContext.class);
	}
	
	public void setPluginInstance(FmtagextWebPlugin plugin){
		instance = (FmtagextWebPlugin)plugin;
	}

	@Override
	public JFishMvcConfigurerListener getJFishMvcConfigurerListener() {
		return new JFishMvcConfigurerListenerAdapter(this){

			@Override
			public void listening(FreeMarkerConfigurerBuildEvent event){
				if(!event.isHasBuilt()){
					event.registerDirective(new JFishEntryDirective())
						.registerDirective(new JFishFieldDirective())
						.registerDirective(new ExtEntityGridDirective(), true)
						.registerDirective(new JFishUIDirective());
				}
			}
		};
	}
}
