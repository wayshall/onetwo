package org.onetwo.common.web.view;

import java.io.Serializable;
import java.util.Map;

public interface ThemeSetting extends Serializable {

	public String getLayoutPage(String path);

	public String getTagPage(String path);

	public String getViewPage(String path);
	
	public String getExtTheme();
	
	public boolean isJsui();
	
	public void setJsui(boolean jsui);
	
//	public boolean supportedDynamicConfig();
	public void config(String theme, String layout, String view);
	public void config(Map<String, Object> config);

}