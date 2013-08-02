package org.onetwo.common.web.view.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

@SuppressWarnings("serial")
public class AbstractBodyTag extends BodyTagSupport {
	
	public static final String VAR_PRIFEX = "__tag__";

	protected String getTagVarName(String name){
		return VAR_PRIFEX + name;
	}
	
	protected <T> T getComponentFromRequest(String name, Class<T> clazz){
		return clazz.cast(pageContext.getRequest().getAttribute(getTagVarName(name)));
	}
	
	protected void setComponentIntoRequest(String name, Object val){
		pageContext.getRequest().setAttribute(getTagVarName(name), val);
	}
	
	protected void clearComponentFromRequest(String name){
		pageContext.getRequest().removeAttribute(getTagVarName(name));
	}

	protected void assertParentTag(Class<?> parentTag) throws JspException{
		assertParentTag(parentTag, "tag["+getClass().getSimpleName()+"] must be a child tag of " + getParent().getClass().getSimpleName());
	}
	
	protected void assertParentTag(Class<?> parentTag, String msg) throws JspException{
		if(!parentTag.isInstance(getParent()))
			throw new JspException(msg);
	}
}
