package org.onetwo.common.web.s2.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.freemarker.tags.TagModel;
import org.onetwo.common.web.s2.tag.component.Parse;

import com.opensymphony.xwork2.util.ValueStack;
 
public class ParseModel extends TagModel{

	public ParseModel(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		super(stack, req, res);
	}

	@Override
	protected Component getBean() {
		return new Parse(stack);
	}

}
