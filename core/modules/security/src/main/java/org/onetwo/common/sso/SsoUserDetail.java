package org.onetwo.common.sso;

import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.SimpleUserDetail;

@SuppressWarnings("serial")
public class SsoUserDetail extends SimpleUserDetail {
	
	private Map<String, Object> attributes = LangUtils.newHashMap();

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void addAttribute(String name, Object val){
		this.attributes.put(name, val);
	}

	public void addAttributes(Map<String, Object> attributes){
		this.attributes.putAll(attributes);
	}
	
}
