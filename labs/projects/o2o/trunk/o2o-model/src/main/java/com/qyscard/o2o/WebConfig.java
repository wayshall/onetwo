package com.qyscard.o2o;

import org.onetwo.common.web.config.BaseSiteConfig;


public class WebConfig {
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
	
	public int getDelCartDay(){
		return siteConfig.getInt("delcard.days");
	}
	
	public int getMoneyDay(){
		return siteConfig.getInt("getmoney.days");
	}
	
	public String getUploadDir(){
		String defDir = siteConfig.getServletContext().getRealPath("/upload");
		return siteConfig.getProperty("upload.dir", defDir);
	}
	
	public String getUploadVisitPath(){
		String path = siteConfig.getVariable("upload.visit.path");
		return path;
	}
	
	public String getExtTheme(){
		return siteConfig.getProperty("ext.theme");
	}
	
	public double getCompanyCommission(){
		String value = siteConfig.getVariable("company.commission");
		return Double.parseDouble(value);
	}
}
