package org.onetwo.plugins.admin.utils;

import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.plugins.security.SecurityPlugin;

final public class AdminPluginConfig {

	public static final String ADMIN_TREEPANEL = "admin.treepanel";
	public static final String ADMIN_MAIN_URL = "admin.main.url";
	public static final String ADMIN_INDEX_TITLE = "admin.index.title";
	public static final String ADMIN_INDEX_VIEW = "admin.index.view";
	public static final String ADMIN_INDEX_ENABLE = "admin.index.enable";
	

	private static final AdminPluginConfig instance = new AdminPluginConfig();
	private BaseSiteConfig config = BaseSiteConfig.getInstance();
	
	private AdminPluginConfig(){
	}

	public static AdminPluginConfig getInstance() {
		return instance;
	}

	public boolean isAdminIndexEnable(){
		return config.getBoolean(ADMIN_INDEX_ENABLE, false);
	}
	
	public String getTreepanel(){
		return config.getProperty(ADMIN_TREEPANEL, "multi");
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
			return config.getBaseURL() + config.getProperty(ADMIN_MAIN_URL);
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
