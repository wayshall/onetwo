package onetwoplugin;

import java.util.List;

import onetwoplugin.model.OnetwopluginContext;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin;



public class OnetwopluginPlugin extends ConfigurableContextPlugin<OnetwopluginPlugin, OnetwopluginConfig> {

	private static OnetwopluginPlugin instance;
	
	
	public static OnetwopluginPlugin getInstance() {
		return instance;
	}
	

	public OnetwopluginPlugin() {
		super("/onetwoplugin/", "onetwoplugin-config");
	}



	@Override
	public void onJFishContextClasses(List<Class<?>> annoClasses) {
		annoClasses.add(OnetwopluginContext.class);
	}

	public void setPluginInstance(OnetwopluginPlugin plugin){
		instance = plugin;
	}

}
