package org.onetwo.common.spring.plugin;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.springframework.core.io.Resource;

/****
 * 有配置文件的插件基类
 * @author wayshall
 *
 * @param <T>
 * @param <C>
 */
abstract public class ConfigurableContextPlugin<T, C extends LoadableConfig> extends AbstractContextPlugin<T> {
	
	public static interface LoadableConfig {
		void load(JFishProperties properties);
		JFishProperties getSourceConfig();
	}

	public static final String CONFIG_POSTFIX = ContextPluginUtils.CONFIG_POSTFIX;
	
	private final String configBaseDir;
	private final String configName;
	private C config;
	private boolean failedIfConfigNotExist;
	

	public ConfigurableContextPlugin(String pluginName, boolean failedIfConfigNotExist) {
		this.configBaseDir = ContextPluginUtils.getConfigBaseDir(pluginName);
		this.configName = ContextPluginUtils.getConfigFileName(pluginName);
		this.config = (C)ReflectUtils.newInstance(ReflectUtils.getSuperClassGenricType(getClass(), 1, ConfigurableContextPlugin.class));
		this.failedIfConfigNotExist = failedIfConfigNotExist;
	}

	public ConfigurableContextPlugin(String configBaseDir, String configName) {
		this(configBaseDir, configName, false);
	}
	public ConfigurableContextPlugin(String configBaseDir, String configName, boolean failedIfConfigNotExist) {
		this(configBaseDir, configName, null, failedIfConfigNotExist);
		this.config = (C)ReflectUtils.newInstance(ReflectUtils.getSuperClassGenricType(getClass(), 1, ConfigurableContextPlugin.class));
	}
	
	public ConfigurableContextPlugin(String configBaseDir, String configName, C config, boolean failedIfConfigNotExist) {
		super();
		this.configBaseDir = StringUtils.surroundWith(configBaseDir, "/");
		this.configName = configName;
		this.config = config;
		this.failedIfConfigNotExist = failedIfConfigNotExist;
	}

	protected void initWithEnv(ContextPluginMeta pluginMeta, String appEnv) {
		if(isConfigExists()){
			JFishProperties properties = ContextPluginUtils.loadConfigs(new String[]{getConfigPath(), getEnvConfigPath(appEnv)});
			config.load(properties);
		}else{
			if(failedIfConfigNotExist)
				throw new BaseException("the plugin["+this.getClass().getName()+"] must be config at: " + configBaseDir+configName);
			//load with empty while config is not exist
			if(config!=null){
				JFishProperties props = new JFishProperties();
				config.load(props);
			}
		}
	}

	public boolean isConfigExists(){
		String path = getConfigPath();
		Resource config = SpringUtils.classpath(path);
		return config.exists();
	}

	public String getConfigPath(){
//		return LangUtils.appendNotBlank(configBaseDir, configName, CONFIG_POSTFIX);
		return ContextPluginUtils.getConfigPath(configBaseDir, configName);
	}
	public String getEnvConfigPath(String env){
//		return LangUtils.appendNotBlank(configBaseDir, configName, "-", env, CONFIG_POSTFIX);
		return ContextPluginUtils.getEnvConfigPath(configBaseDir, configName, env);
	}

	public C getConfig() {
		return config;
	}
}
