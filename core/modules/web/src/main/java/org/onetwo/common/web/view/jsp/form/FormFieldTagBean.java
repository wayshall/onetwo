package org.onetwo.common.web.view.jsp.form;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.HtmlElement;

public class FormFieldTagBean extends HtmlElement {

	private FormFieldType type;
	private boolean errorTag;
	private String render;
	private String value;
	
	private String bodyContent;
	
	private FormTagBean formBean;
	
	
	public boolean isAutoRender(){
		return StringUtils.isBlank(render);
	}
	
	public boolean isHtmlRender(){
		return "html".equals(render);
	}
	
	public FormFieldType getType() {
		return type;
	}
	public void setType(FormFieldType type) {
		this.type = type;
	}
	public boolean isErrorTag() {
		return errorTag;
	}
	public void setErrorTag(boolean errorTag) {
		this.errorTag = errorTag;
	}
	public String getRender() {
		return render;
	}
	public void setRender(String render) {
		this.render = render;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}

	public String getValue() {
		if(StringUtils.isBlank(value))
			return getName();
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FormTagBean getFormBean() {
		return formBean;
	}

	public void setFormBean(FormTagBean formBean) {
		this.formBean = formBean;
	}
	
	public Object getFieldValue(){
		if(this.formBean==null)
			return "";
		return this.formBean.getProvider().getFieldValue(getValue());
	}

	
}
