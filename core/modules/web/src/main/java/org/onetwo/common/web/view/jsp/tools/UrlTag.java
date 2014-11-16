package org.onetwo.common.web.view.jsp.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.preventor.PreventorFactory;
import org.onetwo.common.web.preventor.RequestPreventor;
import org.onetwo.common.web.view.jsp.AbstractTagSupport;

public class UrlTag extends AbstractTagSupport {
	private RequestPreventor csrfPreventor = PreventorFactory.getCsrfPreventor();
	private RequestPreventor repeateSubmitPreventor = PreventorFactory.getRepeateSubmitPreventor();

	private String href;
	private boolean preventSubmit;
	
    public int doEndTag() throws JspException {
    	String url = BaseSiteConfig.getInstance().getBaseURL();
    	if(BaseSiteConfig.getInstance().isSafeRequest()){
    		url += csrfPreventor.processSafeUrl(href, (HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse());
    	}else{
    		url += href;
    	}
		if(preventSubmit){
			url += repeateSubmitPreventor.processSafeUrl(href, (HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse());
		}
    	write(url);
    	return EVAL_PAGE;
    }

	public void setHref(String href) {
		this.href = href;
	}

	public void setPreventSubmit(boolean preventSubmit) {
		this.preventSubmit = preventSubmit;
	}
}
 	