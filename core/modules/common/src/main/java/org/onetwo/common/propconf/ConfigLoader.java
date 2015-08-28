package org.onetwo.common.propconf;

import java.util.Properties;

import org.onetwo.common.propconf.JFishProperties;

public interface ConfigLoader {
	
	Properties load(JFishProperties loadedConfig);

}
