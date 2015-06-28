package org.onetwo.common.web.view.jsp.html;

import org.onetwo.common.web.view.HtmlElement;

public class BoardTagBean extends HtmlElement {

	private String name;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
