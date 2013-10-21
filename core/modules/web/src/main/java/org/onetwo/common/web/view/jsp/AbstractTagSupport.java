package org.onetwo.common.web.view.jsp;

import java.io.IOException;

import javax.servlet.jsp.tagext.TagSupport;

import org.onetwo.common.exception.BaseException;

abstract public class AbstractTagSupport extends TagSupport {

	protected void write(String content){
		try {
			this.pageContext.getOut().write(content);
		} catch (IOException e) {
			throw new BaseException("write content error: " + e.getMessage(), e);
		}
	}
}
