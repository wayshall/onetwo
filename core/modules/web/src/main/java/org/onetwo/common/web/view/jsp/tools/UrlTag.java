package org.onetwo.common.web.view.jsp.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.csrf.CsrfPreventor;
import org.onetwo.common.web.csrf.CsrfPreventorFactory;
import org.onetwo.common.web.view.jsp.AbstractTagSupport;

public class UrlTag extends AbstractTagSupport {
	private CsrfPreventor csrfPreventor = CsrfPreventorFactory.getDefault();

	private String href;
	
    public int doEndTag() throws JspException {
    	String url = BaseSiteConfig.getInstance().getBaseURL();
    	if(BaseSiteConfig.getInstance().isSafeRequest()){
    		url += csrfPreventor.processSafeUrl(href, (HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse());
    	}else{
    		url += href;
    	}
    	write(url);
    	return EVAL_PAGE;
    }

	public void setHref(String href) {
		this.href = href;
	}
    
}
