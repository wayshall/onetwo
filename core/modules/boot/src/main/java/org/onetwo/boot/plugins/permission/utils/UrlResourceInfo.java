package org.onetwo.boot.plugins.permission.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.onetwo.common.utils.StringUtils;
import org.springframework.util.Assert;

@EqualsAndHashCode
public class UrlResourceInfo {
	@Getter @Setter
	private String url;
	@Getter @Setter
	private String method;
	public UrlResourceInfo() {
		super();
	}
	
	public UrlResourceInfo(String url, String method) {
		super();
		Assert.hasText(url);
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
	
	
}
