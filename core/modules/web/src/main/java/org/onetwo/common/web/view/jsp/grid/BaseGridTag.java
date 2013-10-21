package org.onetwo.common.web.view.jsp.grid;

import java.util.Deque;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.HtmlElement;
import org.onetwo.common.web.view.jsp.BaseHtmlTag;

@SuppressWarnings("serial")
abstract public class BaseGridTag<T extends HtmlElement> extends BaseHtmlTag<T> {

	
	protected String getGridVarName(){
		return GridTagBean.class.getSimpleName();
	}
	
	protected String getRowVarName(){
		return RowTagBean.class.getSimpleName();
	}	
}
