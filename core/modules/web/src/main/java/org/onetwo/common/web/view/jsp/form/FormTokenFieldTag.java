package org.onetwo.common.web.view.jsp.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.onetwo.common.web.preventor.PreventorFactory;
import org.onetwo.common.web.preventor.RequestPreventor;
import org.onetwo.common.web.preventor.RequestToken;
import org.onetwo.common.web.view.jsp.AbstractBodyTag;

@SuppressWarnings("serial")
public class FormTokenFieldTag extends AbstractBodyTag {
//	private String name;
	private RequestPreventor csrfPreventor = PreventorFactory.getCsrfPreventor();
	
	private boolean preventSubmit;
	
	@Override
	public int doEndTag() throws JspException {
		RequestToken token = csrfPreventor.generateToken((HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse());
		write("<input name='"+token.getFieldName()+"' type='hidden' value='"+token.getGeneratedValue()+"'/>");
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
