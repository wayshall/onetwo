package org.onetwo.common.web.view.jsp.layout;

import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

@SuppressWarnings("serial")
public class BaseLayoutTag extends BodyTagSupport {
	
	public static class OverrideBody {
		private final String name;
		private final BodyContent content;

		public OverrideBody(String name, BodyContent content) {
			super();
			this.name = name;
			this.content = content;
		}

		public BodyContent getContent() {
			return content;
		}

		public String getContentText() {
			return content.getString();
		}

		public String getName() {
			return name;
		}
		
		
	}
	
	protected static String getOverrideName(String name){
		return "__layout__"+name+"__override__";
	}

	protected OverrideBody getChildPageOverrideBody(String name){
		String overrideName = getOverrideName(name);
		OverrideBody body = (OverrideBody)this.pageContext.getRequest().getAttribute(overrideName);
		return body;
	}
	protected boolean hasChildPageOverride(String name){
		return getChildPageOverrideBody(name)!=null;
	}

	
}
