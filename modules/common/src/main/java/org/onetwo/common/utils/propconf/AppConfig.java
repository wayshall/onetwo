package org.onetwo.common.utils.propconf;

import static org.onetwo.common.utils.propconf.PropUtils.CONFIG_POSTFIX;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.Freezer;
import org.onetwo.common.utils.StringUtils;

public class AppConfig extends PropConfig {

	public static final String APP_NAME = "app.name";
	public static final String APP_ENVIRONMENT = "app.environment";
	/*public static final String APP_ENVIRONMENT_DEV = "dev";
	public static final String APP_ENVIRONMENT_TEST = "test";
	public static final String APP_ENVIRONMENT_PRODUCT = "product";*/
	
	
	public static final String OUTER_CONFIG = "outer.config";
	
	public static AppConfig create(String configName){
		return new AppConfig(configName);
	}
	
	private Map<String, PropConfig> outers = new HashMap<String, PropConfig>();
	
	private Freezer freezer = Freezer.create(this.getClass());
	
	protected AppConfig(String configName) {
		super(configName);
		this.initAppConfig(true);
	}
	
	public Freezer getFreezer() {
		return freezer;
	}

	protected void addConfigByAppEnvironment(){
		String env = this.getAppEnvironment();
		if(StringUtils.isBlank(env))
			return ;
		String configEnv = "";
		if(configName.endsWith(CONFIG_POSTFIX)){
			configEnv = configName.substring(0, configName.length()-CONFIG_POSTFIX.length());
		}else{
			configEnv = configName;
		}
		if(configEnv.endsWith("-base")){
			configEnv = configEnv.substring(0, configEnv.length()-"-base".length());
		}
		if(!Environment.EnvLable.values.containsKey(env)){
			env = Environment.DEV;
		}
		configEnv = configEnv + "-" + env + CONFIG_POSTFIX;
		addConfigFile(configEnv);
	}
	
	protected void loadOuterConfig(){
		List<String> outerFiels = this.getPropertyWithSplit(OUTER_CONFIG, ",");
		for(String outerf : outerFiels){
			PropConfig prop = new PropConfig(outerf);
			prop.initAppConfig(true);
			this.outers.put(outerf, prop);
		}
	}
	
	protected void initAppConfig(){
		this.loadFirstConfig();
		this.addConfigByAppEnvironment();
		this.loadSilent();
		this.loadOuterConfig();
		this.printConfigs();
	}
	
	protected void printConfigs(){
		if(logger.isInfoEnabled()){
			logger.info("==================================== siteconfig start ====================================");
			Enumeration<String> keys = config.configNames();
			String key = null;
			while(keys.hasMoreElements()){
				key = keys.nextElement();
				logger.info(key+": " + this.config.getOriginalProperty(key));
			}
			logger.info("==================================== siteconfig end ====================================");
		}
	}
	
	public String getAppEnvironment(){
		String env = this.getConfig().getOriginalProperty(APP_ENVIRONMENT, Environment.DEV);
		if(!Environment.EnvLable.values.containsKey(env)){
			env = Environment.DEV;
		}
		return env;
//		return this.getProperty(APP_ENVIRONMENT, APP_ENVIRONMENT_DEV);
	}
	
	public String getAppName(){
		return getProperty(APP_NAME, "");
	}
	
	public boolean isDev(){
		return getAppEnvironment().equals(Environment.DEV);
	}
	
	public boolean isProduct(){
		return Environment.PRODUCT.equals(getAppEnvironment());
	}
	
	public boolean isTest(){
		return Environment.TEST.equals(getAppEnvironment());
	}

	public Map<String, PropConfig> getOuters() {
		return outers;
	}

	public PropConfig getOuter(String name) {
		return outers.get(name);
	}

	public boolean isOuter(String name) {
		return outers.containsKey(name);
	}
	
}
