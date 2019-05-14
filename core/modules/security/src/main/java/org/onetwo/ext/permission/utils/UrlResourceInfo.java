package org.onetwo.ext.permission.utils;

import org.onetwo.common.utils.StringUtils;
import org.springframework.util.Assert;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class UrlResourceInfo {
	@Getter @Setter
	private String url;
	@Setter
	private String method;
	public UrlResourceInfo() {
		super();
	}
	
	public UrlResourceInfo(String url, String method) {
		super();
		Assert.hasText(url, "url must has text");
		this.url = url;
		this.method = method;
	}

	public UrlResourceInfo(String url) {
		this(url, null);
	}

	@Override
	public String toString() {
		if(StringUtils.isBlank(method)){
			return url;
		}
		return method + "|" + url;
	}
	
	public String getMethod() {
		if (StringUtils.isNotBlank(method)) {
			return method.toUpperCase();
		}
		return method;
	}
	
	
}
