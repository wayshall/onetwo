package org.onetwo.common.web.s2.ext;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.mapper.DefaultActionMapper;

public class LanguagePathActionMapper extends DefaultActionMapper{
	
	public static final String ATTRIBUTE_KEY = LocaleUtils.ATTRIBUTE_KEY;
	
	protected String getUri(HttpServletRequest request) {
		String uri = super.getUri(request);
		
		String localeStr = "";
		int index = uri.indexOf('/', 1);
		if(index!=-1)
			localeStr = uri.substring(1, index);
		
		if(isSupportThisLanguage(localeStr)){
			uri = uri.substring(index);
		}
		
		return uri;
	}
	
	protected boolean isSupportThisLanguage(String localeStr){
		return LocaleUtils.isSupport(localeStr);
	}
	
	protected Locale getClosestLocale(String localeStr){
		return LocaleUtils.getClosestLocale(localeStr);
	}
}
