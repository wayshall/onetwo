package org.onetwo.common.web.s2.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;

abstract public class WebUIBean extends UIBean {
	
	public static final String DEFAULT_WEBTAG_THEME = "webtag";

	public WebUIBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

    protected void evaluateExtraParams() {
    	super.evaluateExtraParams();
		this.setDefaultUITheme(DEFAULT_WEBTAG_THEME);
    }

}
