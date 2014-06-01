package org.onetwo.common.web.view;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;

public class DefaultTagThemeSetting implements TagThemeSetting {
	
	public static String WEB_INF_DIR = "/WEB-INF";
	public static String BASE_TAG_DIR = "/WEB-INF/tags";
	public static String BASE_LAYOUT_DIR = "/WEB-INF/views/layout/";

	private BaseSiteConfig siteConfig = BaseSiteConfig.getInstance();
	
	@Override
	public String getLayoutPage(String path){
		String layoutPage = path;
		if(StringUtils.isBlank(layoutPage))
			layoutPage = this.getLayout();
		layoutPage = StringUtils.appendEndWith(layoutPage, ".jsp");
		layoutPage = getDirPage(BASE_LAYOUT_DIR, layoutPage);
		return layoutPage;
	}
	
	@Override
	public String getTagPage(String path){
		String t = StringUtils.appendEndWith(path, ".jsp");
		String baseTagDir = getTheme();
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
	
	
	public String getTheme() {
		return siteConfig.getTagTheme();
	}

	public String getLayout() {
		return siteConfig.getLayoutDefaultPage();
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
