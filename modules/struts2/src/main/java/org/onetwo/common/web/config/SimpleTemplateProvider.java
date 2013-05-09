package org.onetwo.common.web.config;

import org.onetwo.common.web.utils.SessionUtils;


public class SimpleTemplateProvider implements TemplateProvider {
	
	public boolean isEnable(){
		return SessionUtils.getSiteInfo()!=null;
	}
	
	public String getTemplateDir(){
		 return SessionUtils.getSiteInfo().getTemplateDir();
	}

}
