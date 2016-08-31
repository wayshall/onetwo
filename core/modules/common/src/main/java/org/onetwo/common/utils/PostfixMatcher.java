package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.Collection;

public class PostfixMatcher {

	private static final String[] DEFAULT_EXCLUDE_SUFFIXS = { ".js", ".css", ".jpg", ".jpeg", ".gif", ".png",".htm" };

	private static final String[] DEFAULT_INCLUDE_SUFFIXS = { ".html", ".jsp", ".do", ".action", ".json", ".xml", ".jfxls" };

	protected Collection<String> excludeSuffixs;
	protected Collection<String> includeSuffixs;
	
	
	public PostfixMatcher() {
		this(null, null);
	}

	public PostfixMatcher(String excludeSuffixsStr, String includeSuffix) {
		String[] excludeSuffixsStrs = StringUtils.split(excludeSuffixsStr, ',');
		if (excludeSuffixsStrs != null && excludeSuffixsStrs.length>0) {
			excludeSuffixs = new ArrayList<String>(excludeSuffixsStrs.length);
			for (String path : excludeSuffixsStrs){
				path = path.trim();
				if(path.indexOf('.')==-1)
					path = "." + path;
				excludeSuffixs.add(path);
			}
		}else{
			excludeSuffixs = CUtils.trimAndexcludeTheClassElement(true, DEFAULT_EXCLUDE_SUFFIXS);
		}

		String[] includeSuffixsStr = StringUtils.split(includeSuffix, ',');
		if (includeSuffixsStr != null && includeSuffixsStr.length>0) {
			includeSuffixs = new ArrayList<String>(includeSuffixsStr.length);
			for (String path : includeSuffixsStr){
				path = path.trim();
				if(path.indexOf('.')==-1)
					path = "." + path;
				includeSuffixs.add(path);
			}
		}else{
			includeSuffixs = CUtils.trimAndexcludeTheClassElement(true, DEFAULT_INCLUDE_SUFFIXS);
		}
	}
	
	public boolean isMatch(String uri) {
		if(!uri.contains(".")){//没有后缀的url
			return true;
		}
		
		for (String suffix : excludeSuffixs) {//此类后缀的将不会经过过滤器
			if (uri.endsWith(suffix))
				return false;
		}
		
		for (String suffix : includeSuffixs) {//此类后缀的必须经过过滤器
			if (uri.endsWith(suffix))
				return true;
		}

		//没有配置的默认不需要过滤
		return false;
	}
	
	
}
