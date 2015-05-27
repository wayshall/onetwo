package org.onetwo.common.utils.propconf;

import java.util.Properties;

import org.onetwo.common.utils.propconf.JFishProperties;

public interface ConfigLoader {
	
	Properties load(JFishProperties loadedConfig);

}
