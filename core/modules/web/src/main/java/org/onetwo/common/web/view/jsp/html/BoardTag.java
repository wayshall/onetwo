package org.onetwo.common.web.view.jsp.html;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.jsp.BaseHtmlTag;

public class BoardTag extends BaseHtmlTag<BoardTagBean> {
	
	private String name;
	private String url;

	@Override
	public BoardTagBean createComponent() {
		BoardTagBean board = new BoardTagBean();
		return board;
	}


	protected void populateComponent() throws JspException{
		super.populateComponent();
		component.setName(name);
		component.setUrl(url);
	}

	protected int endTag()throws Exception {
		//TODO
		return EVAL_PAGE;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void setName(String name) {
		this.name = name;
	}

}
