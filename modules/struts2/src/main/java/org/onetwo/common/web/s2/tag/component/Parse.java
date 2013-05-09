package org.onetwo.common.web.s2.tag.component;

import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.onetwo.common.web.s2.tag.FCKUtils;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("unchecked")
public class Parse extends Component {
	public static final Logger LOG = Logger.getLogger(Parse.class);

	private String defaultValue;
	private String value;
	private boolean escape = true;

	public Parse(ValueStack stack) {
		super(stack);
	}

	@StrutsTagAttribute(description = "The default value to be used if <u>value</u> attribute is null")
	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@StrutsTagAttribute(description = " Whether to escape HTML", type = "Boolean", defaultValue = "true")
	public void setEscape(boolean escape) {
		this.escape = escape;
	}

	@StrutsTagAttribute(description = "Value to be displayed", type = "Object", defaultValue = "&lt;top of stack&gt;")
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean start(Writer writer) {
		boolean result = super.start(writer);

		String actualValue = null;

		if (value == null) {
			value = "top";
		} else {
			value = stripExpressionIfAltSyntax(value);
		}

		actualValue = (String) getStack().findValue(value, String.class);

		try {
			if (actualValue != null) {
				writer.write(prepare(actualValue));
			} else if (defaultValue != null) {
				writer.write(prepare(defaultValue));
			}
		} catch (IOException e) {
			LOG.info("Could not print out value '" + value + "'", e);
		}

		return result;
	}

	public String prepare(String value) {
		value = FCKUtils.decodeFckText(value);
		return value;
	}

}
