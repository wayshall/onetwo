package org.onetwo.common.web.view.jsp.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.onetwo.common.web.csrf.CsrfPreventor;
import org.onetwo.common.web.csrf.CsrfPreventorFactory;
import org.onetwo.common.web.csrf.CsrfToken;
import org.onetwo.common.web.view.jsp.AbstractBodyTag;

@SuppressWarnings("serial")
public class FormTokenFieldTag extends AbstractBodyTag {
//	private String name;
	private CsrfPreventor csrfPreventor = CsrfPreventorFactory.getDefault();
	
	@Override
	public int doEndTag() throws JspException {
		CsrfToken token = csrfPreventor.generateToken((HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse());
		write("<input name='"+csrfPreventor.getFieldOfTokenFieldName()+"' type='hidden' value='"+token.getFieldName()+"'/>");
		write("<input name='"+token.getFieldName()+"' type='hidden' value='"+token.getValue()+"'/>");
		return EVAL_PAGE;
	}
	
	/*public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}*/
	
	

}
