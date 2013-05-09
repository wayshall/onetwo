package stemplate.utils;

import java.util.Map;

import org.onetwo.common.utils.propconf.PropConfig;
import org.onetwo.common.utils.propconf.VariableConfig;
import org.onetwo.common.web.config.BaseSiteConfig;


public class WebConfig {
	
	public static final String PATH_RS = "path.rs";
	
	private static final WebConfig instance;
	
	static {
		instance = new WebConfig();
		instance.siteConfig = BaseSiteConfig.getInstance();
	}
	
	public static WebConfig getInstance(){
		return instance;
	}
	
	private BaseSiteConfig siteConfig;
	
	private WebConfig(){
	}

	public BaseSiteConfig getSiteConfig() {
		return siteConfig;
	}
	
	public String getAppName(){
		return siteConfig.getAppName();
	}
	
	public boolean isDev() {
		return siteConfig.isDev();
	}

	public boolean isProduct() {
		return siteConfig.isProduct();
	}

	public boolean isTest() {
		return siteConfig.isTest();
	}

	public Map<String, PropConfig> getOuters() {
		return siteConfig.getOuters();
	}

	public PropConfig getOuter(String name) {
		return siteConfig.getOuter(name);
	}

	public boolean isOuter(String name) {
		return siteConfig.isOuter(name);
	}

	public void reload() {
		siteConfig.reload();
	}

	public String getProperty(String key, String defaultValue) {
		return siteConfig.getProperty(key, defaultValue);
	}

	public String getBaseURL() {
		return siteConfig.getBaseURL();
	}

	public String getJsPath() {
		return siteConfig.getJsPath();
	}

	public String getCssPath() {
		return siteConfig.getCssPath();
	}

	public String getImagePath() {
		return siteConfig.getImagePath();
	}

	public String getJqueryPath() {
		return siteConfig.getJqueryPath();
	}

	public String getJqueryuiPath() {
		return siteConfig.getJqueryuiPath();
	}

	public String getProperty(String key) {
		return siteConfig.getProperty(key);
	}

	public String getContextPath() {
		return siteConfig.getContextPath();
	}
	
	public String getRsPath() {
		return siteConfig.getRsPath();
	}
	
	public String getSiteConfigName() {
		return siteConfig.getSiteConfigName();
	}

	public VariableConfig getConfig() {
		return siteConfig.getConfig();
	}

	public String getConfigName() {
		return siteConfig.getConfigName();
	}

	public boolean containsKey(Object key) {
		return siteConfig.containsKey(key);
	}

}
