package org.onetwo.common.web.view.jsp.link;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.HtmlElement;

public class LinkTagBean extends HtmlElement {

	private String dataMethod;
	private String dataConfirm;
	private String href;
	private boolean safeUrl;
	private String dataParams;
	
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

	public String getAttributesHtml() {
		String html = super.getAttributesHtml();
		StringBuilder attributesBuf = new StringBuilder(html);
		buildAttributeTag(attributesBuf, "data-method", dataMethod);
		if(StringUtils.isNotBlank(dataMethod)){
			String msg = StringUtils.isBlank(dataConfirm)?"确定要提交此操作？":dataConfirm;
			buildAttributeTag(attributesBuf, "data-confirm", msg);
		}
		return attributesBuf.toString();
	}
	public String getDataParamsAttr() {
		return buildAttributeTag(new StringBuilder(), "data-params", dataParams).toString();
	}
	public String getDataParams() {
		return dataParams;
	}
	public void setDataParams(String dataParams) {
		this.dataParams = dataParams;
	}
	
}
