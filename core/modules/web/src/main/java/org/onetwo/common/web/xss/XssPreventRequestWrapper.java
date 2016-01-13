package org.onetwo.common.web.xss;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.onetwo.common.web.utils.RequestWrapper;

public class XssPreventRequestWrapper extends HttpServletRequestWrapper implements RequestWrapper {

	public XssPreventRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	

	@Override
	public ServletRequest getNativeRequest() {
		ServletRequest req =  super.getRequest();
		while(req instanceof RequestWrapper){
			req = ((RequestWrapper)req).getNativeRequest();
		}
		return req;
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
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> params = super.getParameterMap();
		return new XssEscapeMapWrapper(params);
	}

	@SuppressWarnings("serial")
	public static class XssEscapeMapWrapper extends HashMap<String, String[]> {
//		private final Map<String, String[]> params;

		public XssEscapeMapWrapper(Map<String, String[]> params) {
			super(params);
//			this.params = params;
		}

		@Override
		public String[] get(Object key) {
			String[] values = super.get(key);
			return XssUtils.escapeArray(values);
		}
		
	}
	
	
}
