package org.onetwo.common.web.view.jsp.form;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.HtmlElement;

public class FormFieldTagBean extends HtmlElement {

	private FormFieldType type;
	private boolean errorTag;
	private String value;
	private String dataFormat;
	
	private String bodyContent;
	
	private FormTagBean formBean;
	

	private boolean readOnly;
	private boolean disabled;
	
	private boolean modelAttribute = true;
	
	public boolean isHtmlTypeRender(){
		return FormFieldType.html == type;
	}
	
	public void buildExtTagAttributesString(StringBuilder attributesBuf){
		if(this.readOnly){
			attributesBuf.append("readOnly=\"readOnly\"");
		}
		if(this.disabled){
			attributesBuf.append("disabled");
		}
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
		Object val = this.formBean.getProvider().getFieldValue(this);
		return LangUtils.formatValue(val, getDataFormat());
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isModelAttribute() {
		return modelAttribute;
	}

	public void setModelAttribute(boolean modelAttribute) {
		this.modelAttribute = modelAttribute;
	}

	
}
