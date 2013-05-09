package org.onetwo.common.web.s2.ext;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.utils.StrutsUtils;

@SuppressWarnings("serial")
public class MutiLanguageRedirectResult extends ServletRedirectResult {

	protected void sendRedirect(HttpServletResponse response, String finalLocation) throws IOException {
		finalLocation = appendLanguage(finalLocation); 
		super.sendRedirect(response, finalLocation);
	}

	private static String appendLanguage(String finalLocation) {
		if (finalLocation.indexOf(':') == -1) {
			String contextPath = SiteConfig.getInstance().getContextPath();
			int index = 0;
			StringBuilder path = new StringBuilder(finalLocation);
			if (StringUtils.isNotBlank(contextPath)) {
				index = finalLocation.indexOf(contextPath);
				if (index == -1)
					index = 0;
				else
					index += contextPath.length();
			}
			if(!LocaleUtils.getDefault().equals(StrutsUtils.getCurrentSessionLocale())){
				path.insert(index++, '/');
				path.insert(index, StrutsUtils.getSessionLanguage());
			}
			return path.toString();
		}
		return finalLocation;
	}

}
