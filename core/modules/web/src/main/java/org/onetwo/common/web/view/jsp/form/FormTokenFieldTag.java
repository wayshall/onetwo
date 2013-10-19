package org.onetwo.common.web.view.jsp.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.onetwo.common.web.csrf.CsrfPreventor;
import org.onetwo.common.web.csrf.CsrfPreventor.Token;
import org.onetwo.common.web.view.jsp.AbstractBodyTag;

@SuppressWarnings("serial")
public class FormTokenFieldTag extends AbstractBodyTag {
	private String name;
	private CsrfPreventor csrfPreventor = CsrfPreventor.SESSION;
	
	@Override
	public int doEndTag() throws JspException {
		Token token = csrfPreventor.generateToken(name, (HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse());
		write("<input name='"+token.fieldOfFieldName+"' type='hidden' value='"+token.fieldName+"'/>");
		write("<input name='"+token.fieldName+"' type='hidden' value='"+token.value+"'/>");
		return EVAL_PAGE;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
