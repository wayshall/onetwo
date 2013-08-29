package org.onetwo.common.web.view.jsp.form;

import javax.servlet.jsp.JspException;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.WebContextUtils;
import org.onetwo.common.web.view.jsp.AbstractBodyTag;

@SuppressWarnings("serial")
public class FormTokenFieldTag extends AbstractBodyTag {
	private static final String DEFAULT_META_NAME = WebContextUtils.DEFAULT_TOKEN_FIELD_NAME;
	private String name;
	
	
	@Override
	public int doEndTag() throws JspException {
		String tname = StringUtils.isBlank(name)?LangUtils.generateToken():name;
		String tokenValue = LangUtils.generateToken(tname);
		WebContextUtils.attr(this.pageContext.getSession(), tname, tokenValue);
		write("<input name='"+DEFAULT_META_NAME+"' type='hidden' value='"+tname+"'/>");
		write("<input name='"+tname+"' type='hidden' value='"+tokenValue+"'/>");
		return EVAL_PAGE;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
