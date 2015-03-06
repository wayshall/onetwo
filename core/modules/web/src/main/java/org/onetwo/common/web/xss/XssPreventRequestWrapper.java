package org.onetwo.common.web.xss;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssPreventRequestWrapper extends HttpServletRequestWrapper {

	public XssPreventRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if(value==null)
			return null;
		return XssUtils.escapeIfNeccessary(value).toString();
	}

    public String[] getParameterValues(String name){
    	String[] values = super.getParameterValues(name);
    	return XssUtils.escapeArray(values);
    }

	@Override
	public Map getParameterMap() {
		Map<String, String[]> params = super.getParameterMap();
		return new XssEscapeMapWrapper(params);
	}

	public static class XssEscapeMapWrapper extends HashMap<String, String[]> {
		private final Map<String, String[]> params;

		public XssEscapeMapWrapper(Map params) {
			super();
			this.params = params;
		}

		@Override
		public String[] get(Object key) {
			String[] values = super.get(key);
			return XssUtils.escapeArray(values);
		}
		
	}
	
	
}
