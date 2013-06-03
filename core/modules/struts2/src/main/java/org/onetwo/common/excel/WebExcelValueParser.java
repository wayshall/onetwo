package org.onetwo.common.excel;

import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("unchecked")
public class WebExcelValueParser extends DefaultExcelValueParser {

	private ValueStack stack;

	public WebExcelValueParser(Map context) {
		stack = ServletActionContext.getValueStack(StrutsUtils.getRequest());
		stack.getContext().putAll(context);
	}
	
	public Object parseValue(String exp, Object root) {
		boolean push = (root!=null);
		Object value = null;
		try {
			if(push)
				this.stack.push(root);
			value = stack.findValue(exp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(push)
				this.stack.pop();
		}
		return value;
	}

	public ValueStack getStack() {
		return stack;
	}

	public Map getContext() {
		return this.stack.getContext();
	}

}
