package org.onetwo.common.web.s2.tag.webtag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.components.UIBean;
import org.onetwo.common.web.utils.StrutsUtils;
import org.springframework.util.Assert;

import com.opensymphony.xwork2.util.ValueStack;

public class SimpleUIBean extends UIBean{
	
	protected String defaultTemplate;
	
	protected Component component;

	public SimpleUIBean(Component comp) {
		this(comp.getStack(), StrutsUtils.getRequest(), StrutsUtils.getResponse());
		Assert.notNull(comp);
		this.component = comp;
		this.parameters = this.component.getParameters();
	}

	public SimpleUIBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	@Override
	protected String getDefaultTemplate() {
		return defaultTemplate;
	}

	public void setDefaultTemplate(String defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
	}

	public Component getComponent() {
		return component;
	}

}
