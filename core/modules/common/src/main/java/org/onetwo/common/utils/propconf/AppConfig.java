package org.onetwo.common.utils.propconf;

import static org.onetwo.common.utils.propconf.PropUtils.CONFIG_POSTFIX;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.onetwo.common.utils.Freezer;
import org.onetwo.common.utils.ReflectUtils;

public class AppConfig extends PropConfig {

	public static final String APP_NAME = "app.name";
	public static final String APP_CODE = "app.code";
	public static final String APP_ENVIRONMENT = "app.environment";
	public static final String JFISH_BASE_PACKAGES = "jfish.base.packages";
	public static final String CONFIG_LOADER = "config.loader";
	/*public static final String APP_ENVIRONMENT_DEV = "dev";
	public static final String APP_ENVIRONMENT_TEST = "test";
	public static final String APP_ENVIRONMENT_PRODUCT = "product";*/
	
	
	public static final String OUTER_CONFIG = "outer.config";
	
	public static AppConfig create(String configName){
		return new AppConfig(configName);
	}
	
	private Map<String, PropConfig> outers = new HashMap<String, PropConfig>();
	
	private Freezer freezer = Freezer.create(this.getClass());
	private ConfigLoader configLoader;
	
	protected AppConfig(String configName) {
		super(configName);
		this.initAppConfig(true);
	}

	@Override
	protected void afterInitAppConfig(JFishProperties loadedConfig){
		Class<ConfigLoader> loaderClass = loadedConfig.getClass(CONFIG_LOADER, null);
		if(loaderClass!=null){
			logger.info("load config again by loader: " + loaderClass);
			configLoader = ReflectUtils.newInstance(loaderClass);
			Properties properties = configLoader.load(loadedConfig);
			loadedConfig.putAll(properties);
		}
	}
	
	public Freezer getFreezer() {
		return freezer;
	}

	/****
	 * 根据第一个加载的配置文件名称来加载相应的环境配置
	 * ${configName}-${env}.properties
	 */
	protected void addConfigByAppEnvironment(){
		String env = this.getAppEnvironment();
		String configEnv = "";
		if(configName.endsWith(CONFIG_POSTFIX)){
			configEnv = configName.substring(0, configName.length()-CONFIG_POSTFIX.length());
		}else{
			configEnv = configName;
		}
		if(configEnv.endsWith("-base")){
			configEnv = configEnv.substring(0, configEnv.length()-"-base".length());
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
				logger.info(key+": " + this.config.getProperty(key));
			}
			logger.info("==================================== siteconfig end ====================================");
		}
	}

	public String getAppEnvironment(){
		String env = this.getConfig().getProperty(APP_ENVIRONMENT, Env.DEV.getValue());
		return Env.of(env).getValue();
//		return this.getProperty(APP_ENVIRONMENT, APP_ENVIRONMENT_DEV);
	}
	public Env getEnv(){
		return Env.of(getAppEnvironment());
//		return this.getProperty(APP_ENVIRONMENT, APP_ENVIRONMENT_DEV);
	}
	
	public String getAppName(){
		return getProperty(APP_NAME, "");
	}
	
	public String getAppCode(){
		return getProperty(APP_CODE, getAppName());
	}
	
	public boolean isDev(){
		return getEnv()==Env.DEV;
	}
	
	public boolean isProduct(){
		return getEnv()==Env.PRODUCT;
	}
	
	public boolean isTest(){
		return getEnv()==Env.TEST;
	}
	
	public String getJFishBasePackages(){
		return getProperty(JFISH_BASE_PACKAGES);
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
