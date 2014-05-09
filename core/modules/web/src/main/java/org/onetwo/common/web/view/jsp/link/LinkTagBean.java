package org.onetwo.common.web.view.jsp.link;

import org.onetwo.common.web.view.HtmlElement;

public class LinkTagBean extends HtmlElement {

	private String dataMethod;
	private String dataConfirm;
	private String href;
	private boolean safeUrl;
	
	public String getDataMethod() {
		return dataMethod;
	}
	public void setDataMethod(String dataMethod) {
		this.dataMethod = dataMethod;
	}
	public String getDataConfirm() {
		return dataConfirm;
	}
	public void setDataConfirm(String dataConfirm) {
		this.dataConfirm = dataConfirm;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public boolean isSafeUrl() {
		return safeUrl;
	}
	public void setSafeUrl(boolean safeUrl) {
		this.safeUrl = safeUrl;
	}
	
}
