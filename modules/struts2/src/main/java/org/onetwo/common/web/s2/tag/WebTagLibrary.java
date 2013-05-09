package org.onetwo.common.web.s2.tag;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.TagLibrary;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("unchecked")
public class WebTagLibrary implements TagLibrary {

	@Override
	public Object getFreemarkerModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new WebTagModels(stack, req, res);
	}

	@Override
	public List<Class> getVelocityDirectiveClasses() {
		return null;
	}

}
