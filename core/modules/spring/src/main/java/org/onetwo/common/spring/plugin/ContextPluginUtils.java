package org.onetwo.common.spring.plugin;

import java.io.IOException;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

public class ContextPluginUtils {
	public static final String BASE_DIR = "/plugins/";
	public static final String CONFIG_POSTFIX = ".properties";
	public static final String CONFIG_NAME_POSTFIX = "-config";
	

	public static JFishProperties loadPluginConfigs(String pluginName, String env){
		return loadConfigs(getEnvConfigPaths(pluginName, env));
	}
	public static JFishProperties loadPluginConfigs(JFishProperties properties, String pluginName, String env){
		return loadConfigs(properties, getEnvConfigPaths(pluginName, env));
	}

	public static <T extends JFishProperties> T loadConfigs(String...classpaths){
		return (T)loadConfigs(new JFishProperties(), classpaths);
	}
	public static <T extends JFishProperties> T loadConfigs(T properties, String...classpaths){
		PropertiesFactoryBean pfb = SpringUtils.createPropertiesBySptring(properties, classpaths);
		try {
			pfb.afterPropertiesSet();
			T rs = (T)pfb.getObject();
			return rs;
		} catch (IOException e) {
			throw new BaseException("load config error: " + e.getMessage(), e);
		}
	}
	
	public static String[] getEnvConfigPaths(String pluginName, String env){
		return new String[]{getConfigPath(null, pluginName), getEnvConfigPath(null, pluginName, env)};
	}

	public static String getConfigPath(String configBaseDir, final String pluginName){
		return getEnvConfigPath(configBaseDir, pluginName, null);
	}

	/*****
	 * 
	 * @param configBaseDir
	 * @param pluginName
	 * @param env
	 * @return /plugins/pluginName-config.properties
	 */
	public static String getEnvConfigPath(String configBaseDir, final String pluginName, String env){
		if(StringUtils.isBlank(configBaseDir)){
			//trim  -config
			configBaseDir = getConfigBaseDir(pluginName);
		}
		//add -config 
		String configName = getConfigFileName(pluginName);
		
		StringBuilder str = new StringBuilder();
		str.append(configBaseDir).append(configName);
		
		//add env postfix
		if(StringUtils.isNotBlank(env)){
			str.append("-").append(env);
		}
		str.append(CONFIG_POSTFIX);
		return str.toString();
	}
	
	public static String getConfigBaseDir(String pluginName){
		//trim  -config, just for compatibility
		String configBaseDir = StringUtils.trimEndWith(pluginName, CONFIG_NAME_POSTFIX);
		configBaseDir = BASE_DIR + StringUtils.appendEndWith(configBaseDir, "/");
		return configBaseDir;
	}
	
	public static String getConfigFileName(String pluginName){
		//add  -config, just for compatibility
		String configName = StringUtils.appendEndWith(pluginName, CONFIG_NAME_POSTFIX);
		return configName;
	}
	
	private ContextPluginUtils(){
	}

}
