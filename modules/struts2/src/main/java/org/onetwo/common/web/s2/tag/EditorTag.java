package org.onetwo.common.web.s2.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;
import org.onetwo.common.web.s2.tag.component.Editor;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class EditorTag extends AbstractUITag{
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new Editor(stack, req, res);
	}

	@Override
	protected void populateParams() {
		super.populateParams();
	}

}
