package org.onetwo.common.web.view.jsp.link;

import javax.servlet.jsp.JspException;

import org.onetwo.common.web.view.jsp.BaseHtmlTag;
import org.onetwo.common.web.view.jsp.TagUtils;

public class LinkTag extends BaseHtmlTag<LinkTagBean>{

	private String template = TagUtils.getTagPage("html/link.jsp");
	
	private String dataMethod;
	private String dataConfirm;
	private String href;
	
	@Override
	public LinkTagBean createComponent() {
		return new LinkTagBean();
	}

	@Override
	protected void populateComponent() throws JspException {
		super.populateComponent();
		component.setDataConfirm(dataConfirm);
		component.setDataMethod(dataMethod);
		component.setHref(href);

		setComponentIntoRequest(this.getClass().getSimpleName(), component);
	}

	protected int endTag()throws Exception {
		try {
			this.pageContext.include(getTemplate());
		} finally {
			clearComponentFromRequest(this.getClass().getSimpleName());
		}
		return EVAL_PAGE;
	}

	public void setDataMethod(String dataMethod) {
		this.dataMethod = dataMethod;
	}

	public void setDataConfirm(String dataConfirm) {
		this.dataConfirm = dataConfirm;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
