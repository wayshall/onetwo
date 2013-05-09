package org.onetwo.common.web.s2.tag.webtag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.IteratorTag;
import org.onetwo.common.web.s2.tag.webtag.compenent.Ajax;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class AjaxTag extends IteratorTag {

	private String href;
	
	protected void populateParams(){
		super.populateParams();
		Ajax ajax = (Ajax)getComponent();
		ajax.setHref(href);
	}

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		Ajax ajax = new Ajax(stack);
		return ajax;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}
