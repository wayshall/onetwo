package org.onetwo.plugins.permission.tag;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.jsp.AbstractBodyTag;
import org.onetwo.plugins.permission.PermissionUtils;

@SuppressWarnings("serial")
public class HasPermissionTag extends AbstractBodyTag {

	private String code;
	
	@Override
	public int doStartTag() throws JspException {
		if(PermissionUtils.hasPermission(code)){
			return EVAL_BODY_INCLUDE;
		}else{
			return SKIP_BODY;
		}
	}
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
