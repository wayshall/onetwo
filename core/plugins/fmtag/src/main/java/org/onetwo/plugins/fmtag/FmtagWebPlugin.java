package org.onetwo.plugins.fmtag;

import java.util.List;

import org.onetwo.common.fish.plugin.AbstractJFishPlugin;
import org.onetwo.common.fish.plugin.JFishMvcConfigurerListenerAdapter;
import org.onetwo.common.spring.web.mvc.config.JFishMvcConfigurerListener;
import org.onetwo.common.spring.web.mvc.config.event.FreeMarkerConfigurerBuildEvent;
import org.onetwo.plugins.fmtag.directive.DataComponentDirective;
import org.onetwo.plugins.fmtag.directive.DataFieldDirective;
import org.onetwo.plugins.fmtag.directive.DataGridDirective;
import org.onetwo.plugins.fmtag.directive.DataRowDirective;
import org.onetwo.plugins.fmtag.directive.VarDirective;


public class FmtagWebPlugin extends AbstractJFishPlugin<FmtagWebPlugin> {

	private static FmtagWebPlugin instance;
	
	
	public static FmtagWebPlugin getInstance() {
		return instance;
	}
	

	public static String getTemplatePath(String template) {
		return getInstance().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}

	@Override
	public void onMvcContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(FmtagContext.class);
	}

	@Override
	public void setPluginInstance(FmtagWebPlugin plugin){
		instance = plugin;
	}


	@Override
	public JFishMvcConfigurerListener getJFishMvcConfigurerListener() {
		return new JFishMvcConfigurerListenerAdapter(this){

			@Override
			public void listening(FreeMarkerConfigurerBuildEvent event){
				if(!event.isHasBuilt()){
					event.registerDirective(new DataGridDirective())
						.registerDirective(new DataRowDirective()) 
						.registerDirective(new DataFieldDirective()) 
						.registerDirective(new DataComponentDirective())
						.registerDirective(new VarDirective());
				}
			}
		};
	}

	@Override
	public boolean registerMvcResources() {
		return true;
	}

}
