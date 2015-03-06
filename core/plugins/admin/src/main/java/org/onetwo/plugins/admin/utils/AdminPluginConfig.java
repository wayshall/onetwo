package org.onetwo.plugins.admin.utils;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.plugins.security.SecurityPlugin;

final public class AdminPluginConfig implements LoadableConfig {

	public static final String ADMIN_THEME = "admin.theme";
	public static final String ADMIN_TREEPANEL = "admin.treepanel";
	public static final String ADMIN_MAIN_URL = "admin.main.url";
	public static final String ADMIN_INDEX_TITLE = "admin.index.title";
	public static final String ADMIN_INDEX_VIEW = "admin.index.view";
	public static final String ADMIN_INDEX_ENABLE = "admin.index.enable";
	public static final String ADMIN_MODULE_ENABLE = "admin.module.enable";
	public static final String DATA_MODULE_ENABLE = "data.module.enable";
	

//	private static final AdminPluginConfig instance = new AdminPluginConfig();
//	private BaseSiteConfig config = BaseSiteConfig.getInstance();

	private JFishProperties config;
	
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
	}

	@Override
	public JFishProperties getSourceConfig() {
		return config;
	}

	public AdminPluginConfig(){
	}

	/*public static AdminPluginConfig getInstance() {
		return instance;
	}*/

	public boolean isAdminIndexEnable(){
		return config.getBoolean(ADMIN_INDEX_ENABLE, false);
	}

	public boolean isAdminModuleEnable(){
		return config.getBoolean(ADMIN_MODULE_ENABLE, false);
	}
	public boolean isDataModuleEnable(){
		return config.getBoolean(DATA_MODULE_ENABLE, false);
	}
	
	public String getTreepanel(){
		return config.getProperty(ADMIN_TREEPANEL, "multi");
	}
	public String getTheme(){
		return config.getProperty(ADMIN_THEME, "neptune");//gray
	}
	
	public boolean isSinglePanel(){
		return "single".equals(getTreepanel());
	}
	
	/*public String getTitle(){
		return BaseSiteConfig.getInstance().getProperty(ADMIN_INDEX_TITLE, "管理后台");
	}*/
	public String getAdminView(){
		return BaseSiteConfig.getInstance().getProperty(ADMIN_INDEX_VIEW);
	}
	public String getMainUrl(){
		if(config.containsKey(ADMIN_MAIN_URL))
			return BaseSiteConfig.getInstance().getBaseURL() + config.getProperty(ADMIN_MAIN_URL);
		else
			return "";
	}
	
	public String getServerLogoutUrl(){
		if(SecurityPlugin.getInstance().isSsoEnable()){
			return SecurityPlugin.getInstance().getSsoConfig().getServerLogoutUrl();
		}else{
			return BaseSiteConfig.getInstance().getBaseURL() + BaseSiteConfig.getInstance().getLogoutUrl();
		}
	}
}
