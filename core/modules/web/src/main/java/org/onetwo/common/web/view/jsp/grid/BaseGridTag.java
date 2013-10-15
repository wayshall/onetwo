package org.onetwo.common.web.view.jsp.grid;

import java.util.Deque;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.HtmlElement;
import org.onetwo.common.web.view.jsp.BaseHtmlTag;

@SuppressWarnings("serial")
abstract public class BaseGridTag<T extends HtmlElement> extends BaseHtmlTag<T> {

	private static final String TAG_STACK_NAME = "tagStack";
	
	protected void setTagStack(Deque<HtmlElement> tagStack){
		setComponentIntoRequest(TAG_STACK_NAME, tagStack);
	}
	protected Deque<HtmlElement> getTagStack(){
		return (Deque<HtmlElement>)getComponentFromRequest(TAG_STACK_NAME, Deque.class);
	}
	protected void clearTagStackFromRequest(){
		clearComponentFromRequest(TAG_STACK_NAME);
	}
	

	@Override
	public int doStartTag() throws JspException {
		int rs = startTag();
		getTagStack().push(component);
		return rs;
	}

	@Override
	public int doEndTag() throws JspException {
		int rs = endTag();
		getTagStack().pop();
		return rs;
	}
	
	protected String getGridVarName(){
		return GridTagBean.class.getSimpleName();
	}
	
	protected String getRowVarName(){
		return RowTagBean.class.getSimpleName();
	}	
}
