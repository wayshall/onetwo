package org.onetwo.common.utils;


public class SiteInfo {
	
	public static final String KEY = "siteInfo";
	
	protected String template;
	
	public boolean hasTemplate(){
		return StringUtils.isNotBlank(getTemplate());
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	

}
