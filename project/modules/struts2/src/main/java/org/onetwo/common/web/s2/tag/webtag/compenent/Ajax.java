package org.onetwo.common.web.s2.tag.webtag.compenent;

import java.io.InputStream;
import java.net.URL;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.components.IteratorComponent;
import org.onetwo.common.utils.json.JSONUtils;
import org.onetwo.common.utils.propconf.VariableSupporter;

import com.opensymphony.xwork2.util.ValueStack;

public class Ajax extends IteratorComponent {

	private String href;
	
	public Ajax(ValueStack stack) {
		super(stack);
	}
	
	@Override
	protected Object findValue(String expr) {
		try {
			Object value = super.findValue(href);
			
			if(value instanceof String) {
				href = value.toString();
			} else if (value == null && href.startsWith("#")) {
				String v = stack.findString(href);
				if (v == null) {
					int start = href.indexOf("#");
					int end = href.indexOf(".");
					String configName = href.substring(start + 1, end > -1 ? end:href.length());
					Object instance = ServletActionContext.getServletContext().getAttribute(configName);
					
					if (instance instanceof VariableSupporter) {
						stack.getContext().put(configName, instance);
						v = stack.findString(href);
					}
				}
				href = v;
			}
			
			URL url = new URL(href);
			InputStream is = url.openStream();
			
			StringBuffer sb = new StringBuffer();
			
			int cache;
			while((cache = is.read()) != -1) {
				sb.append((char)cache);
			}
			
			String jsonString = sb.toString();
			
			if(JSONUtils.isJsonString(jsonString)) {
				JSONArray json = JSONUtils.getJSONArray(jsonString);
				return json.toArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setHref(String href) {
		this.href = href;
	}

	public String getHref() {
		return href;
	}


}
