package onetwoplugin;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

public class OnetwopluginConfig implements LoadableConfig {

	private JFishProperties config;
	

	public OnetwopluginConfig() {
	}
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
	}


	@Override
	public JFishProperties getSourceConfig() {
		return config;
	}

}
