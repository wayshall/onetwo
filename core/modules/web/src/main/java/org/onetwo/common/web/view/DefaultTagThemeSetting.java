package org.onetwo.common.web.view;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.view.jsp.TagUtils;

public class DefaultTagThemeSetting implements ThemeSetting {
	
	public static String WEB_INF_DIR = "/WEB-INF";
	public static String BASE_TAG_DIR = "/WEB-INF/tags";
	public static String BASE_LAYOUT_DIR = "layout/";

	private BaseSiteConfig siteConfig = BaseSiteConfig.getInstance();
	private String themeTag = siteConfig.getThemeTag();
	private String themeLayoutDefaultPage = siteConfig.getThemeLayoutDefaultPage();
	private String themeView = siteConfig.getThemeView();
	
	@Override
	public String getViewPage(String path) {
		return getThemeView() + path;
	}

	@Override
	public String getLayoutPage(String path){
		String layoutPage = path;
		if(path!=null && path.startsWith("/")){
			layoutPage = TagUtils.getViewPage(path);
		}else{
			if(StringUtils.isBlank(layoutPage))
				layoutPage = this.getThemeLayoutDefaultPage();
			String baseLayoutDir = WEB_INF_DIR + getThemeView() + BASE_LAYOUT_DIR;
			layoutPage = StringUtils.appendEndWith(layoutPage, ".jsp");
			layoutPage = getDirPage(baseLayoutDir, layoutPage);
		}
		return layoutPage;
	}
	
	@Override
	public String getTagPage(String path){
		String t = StringUtils.appendEndWith(path, ".jsp");
		String baseTagDir = getThemeTag();
		baseTagDir = StringUtils.appendEndWith(baseTagDir, "/");
		if(StringUtils.isBlank(baseTagDir)){
			baseTagDir = BASE_TAG_DIR;
		}else if(baseTagDir.startsWith("/tags/")){
			baseTagDir = WEB_INF_DIR + baseTagDir;
		}else{
			baseTagDir = BASE_TAG_DIR + baseTagDir;
		}
		return getDirPage(baseTagDir, t);
	}
	
	
	public String getThemeView() {
		return themeView;
	}

	public String getThemeTag() {
		return this.themeTag;
	}

	public String getThemeLayoutDefaultPage() {
		return this.themeLayoutDefaultPage;
	}

	protected String getDirPage(String baseDir, String path){
		if(StringUtils.isBlank(path))
			return path;
		
		baseDir = StringUtils.appendEndWith(baseDir, "/");
		if(path.startsWith(baseDir))
			return path;
		
		if(path.startsWith("/"))
			path = path.substring(1);
		
		return baseDir + path;
	}
}
