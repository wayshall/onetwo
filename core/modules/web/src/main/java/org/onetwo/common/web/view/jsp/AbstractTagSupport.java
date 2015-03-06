package org.onetwo.common.web.view.jsp;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;

abstract public class AbstractTagSupport extends TagSupport implements DynamicAttributes{

	private Map<String, Object> dynamicAttributes = LangUtils.newHashMap();
	
	protected void write(String content){
		try {
			this.pageContext.getOut().write(content);
		} catch (IOException e) {
			throw new BaseException("write content error: " + e.getMessage(), e);
		}
	}

	@Override
	public void setDynamicAttribute(String uri, String localName, Object value)
			throws JspException {
		this.dynamicAttributes.put(localName, value);
	}

	public Object getDynamicAttribute(String attr) {
		if(LangUtils.isEmpty(dynamicAttributes))
			return null;
		return dynamicAttributes.get(attr);
	}
	
	
}
