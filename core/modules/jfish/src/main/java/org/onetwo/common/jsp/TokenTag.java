package org.onetwo.common.jsp;

import javax.servlet.jsp.JspException;

import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.AbstractBodyTag;

@SuppressWarnings("serial")
public class TokenTag extends AbstractBodyTag {
	private String name;
	
	
	@Override
	public int doEndTag() throws JspException {
		String tname = StringUtils.isBlank(name)?LangUtils.generateToken():name;
		String tokenValue = LangUtils.generateToken(tname);
		JFishWebUtils.session(tname, tokenValue);
		write("<input name='"+JFishWebUtils.DEFAULT_TOKEN_FIELD_NAME+"' type='hidden' value='"+tname+"'/>");
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
