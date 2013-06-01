package org.onetwo.common.web.s2.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;
import org.onetwo.common.web.s2.tag.component.Parse;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class ParseTag extends ComponentTagSupport {
    private String defaultValue;
    private String value;
    private boolean escape = true;
	private boolean reverse;
    
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new Parse(stack);
	}

    protected void populateParams() {
        super.populateParams();

        Parse tag = (Parse) component;
        tag.setDefault(defaultValue);
        tag.setValue(value);
        tag.setEscape(escape);
    }

    public void setDefault(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setEscape(boolean escape) {
        this.escape = escape;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
}
