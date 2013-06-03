package org.onetwo.common.web.utils;

import java.io.Serializable;

import org.onetwo.common.utils.SiteInfo;
import org.onetwo.common.web.config.SiteConfig;

public class WebSiteInfo extends SiteInfo implements Serializable {

	public String getTemplateDir() {
		String tempdir = SiteConfig.getInstance().getTemplateDir();
		if(tempdir==null)
			return null;
		if(!tempdir.endsWith("/"))
			tempdir += "/";
		return tempdir + template;
	}

}
