package org.onetwo.common.web.s2.tag.component;

import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("unchecked")
public class StrutsPage extends TableUIBean {

	public StrutsPage(ValueStack stack, HttpServletRequest request,
			HttpServletResponse response) {
		super(stack, request, response);
	}

	public static final String TEMPLATE = "page";
	public static final String TEMPLATE_CLOSE = "page-close";
	
	protected Set<Object> notParams = new HashSet<Object>();
	
	protected String form;
	
	@Override
	public boolean end(Writer writer, String body) {
		StringBuffer uri = new StringBuffer(request.getAttribute("javax.servlet.forward.request_uri").toString());
		Iterator<Entry> ite = this.getParameters().entrySet().iterator();
		
		boolean first = true;
		while(ite.hasNext()) {
			Entry e = ite.next();
			if(notParams.contains(e.getKey()) || e.getKey().equals("pr.pageNo")) {
				continue;
			}
			
			if(first) {
				uri.append("?").append(e.getKey()).append("=").append(e.getValue());
				first = false;
			} else {
				uri.append("&").append(e.getKey()).append("=").append(e.getValue());
			}
		}
		
		uri.append(first ? "?" : "&");
		
		this.addParameter("action", uri.toString());
		return super.end(writer, body);
	}
	
	@Override
	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		
		this.addParameter("page", this.findValue(value));
		
		if(form != null && form.length() != 0) {
			this.addParameter("isForm", true);
			this.addParameter("form", form);
		}
		
		
		notParams.addAll(this.getParameters().keySet());
	}

	@Override
	public String getDefaultOpenTemplate() {
		return TEMPLATE;
	}

	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE_CLOSE;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

}
