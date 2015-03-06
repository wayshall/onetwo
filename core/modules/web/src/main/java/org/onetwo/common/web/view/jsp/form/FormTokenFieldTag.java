package org.onetwo.common.web.view.jsp.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.onetwo.common.web.config.BaseSiteConfig;
import org.onetwo.common.web.preventor.PreventorFactory;
import org.onetwo.common.web.preventor.RequestPreventor;
import org.onetwo.common.web.preventor.RequestToken;
import org.onetwo.common.web.view.jsp.AbstractBodyTag;

@SuppressWarnings("serial")
public class FormTokenFieldTag extends AbstractBodyTag {
//	private String name;
	private RequestPreventor csrfPreventor = PreventorFactory.getCsrfPreventor();
	private RequestPreventor submitPreventor = PreventorFactory.getRepeateSubmitPreventor();
	
	private boolean preventSubmit = true;
	
	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();
		HttpServletResponse resp = (HttpServletResponse)pageContext.getResponse();
		if(BaseSiteConfig.getInstance().isSafeRequest()){
			RequestToken token = csrfPreventor.generateToken(req, resp);
			write("<input name='"+token.getFieldName()+"' type='hidden' value='"+token.getGeneratedValue(csrfPreventor.getTokenValueGenerator())+"'/>");
		}
		if(BaseSiteConfig.getInstance().isPreventRepeateSubmit() && preventSubmit){
			RequestToken submitToken = submitPreventor.generateToken(req, resp);
			write("<input name='"+submitToken.getFieldName()+"' type='hidden' value='"+submitToken.getGeneratedValue(submitPreventor.getTokenValueGenerator())+"'/>");
		}
		return EVAL_PAGE;
	}

	public void setPreventSubmit(boolean preventSubmit) {
		this.preventSubmit = preventSubmit;
	}
	
	/*public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}*/
	
	

}
