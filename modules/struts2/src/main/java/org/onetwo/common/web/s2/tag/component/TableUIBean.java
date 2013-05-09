package org.onetwo.common.web.s2.tag.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.onetwo.common.web.s2.tag.WebUIClosingBean;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("unchecked")
public abstract class TableUIBean extends WebUIClosingBean{
	protected Logger logger = Logger.getLogger(this.getClass());

	public static final String DEFAULT_UI_THEME = "strutspage";

    public TableUIBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		this.defaultUITheme = DEFAULT_UI_THEME;
	}
}
