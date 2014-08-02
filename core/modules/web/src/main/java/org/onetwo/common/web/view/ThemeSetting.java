package org.onetwo.common.web.view;

public interface ThemeSetting {

	public String getLayoutPage(String path);

	public String getTagPage(String path);

	public String getViewPage(String path);
	
	public String getExtTheme();
	
	public boolean isJsui();
	
	public void setJsui(boolean jsui);

}