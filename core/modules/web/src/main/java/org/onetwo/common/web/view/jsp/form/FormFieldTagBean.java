package org.onetwo.common.web.view.jsp.form;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.HtmlElement;
import org.slf4j.Logger;

public class FormFieldTagBean extends HtmlElement {

	private static final Logger logger = MyLoggerFactory.getLogger(FormFieldTagBean.class);
	
	private FormFieldType type;
	private boolean errorTag;
	private String errorPath;
	
	private String value;
	private String dataFormat;
	
	private String bodyContent;
	
	private FormTagBean formBean;
	

	private boolean readOnly;
	private boolean disabled;
	
	private boolean modelAttribute = true;
	private boolean showLoadingText = true;
	
	public boolean isHtmlTypeRender(){
		return FormFieldType.html == type;
	}
	
	public void buildExtTagAttributesString(StringBuilder attributesBuf){
		if(this.isReadOnly()){
			attributesBuf.append("readOnly=\"readOnly\"");
		}
		if(this.isDisabled()){
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
//		if("consumeRate".equals(name))
//			System.out.println("errortag: " + errorPath);
		return errorTag && getErrorPath()!=null && this.formBean.getProvider().isReadableProperty(getErrorPath());
	}
	public void setErrorTag(boolean errorTag) {
//		if("consumeRate".equals(name))
//			System.out.println("errortag: " + errorPath);
		this.errorTag = errorTag;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}

	public String getValue() {
		//如果value属性没有设置过，使用name的名称作为属性
		if(value==null)
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
		Object val = null;
		try {
			val = this.formBean.getProvider().getFieldValue(this);
		} catch (Exception e) {
			throw new BaseException("getFieldValue error", e);
		}
		val = LangUtils.formatValue(val, getDataFormat());
		return val==null?"":val;
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
		return this.formBean.isShowOnly() || disabled;
	}

	public boolean isHidden() {
		return FormFieldType.hidden==type;
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

	public String getErrorPath() {
		if(StringUtils.isBlank(errorPath))
			return getName();
		return errorPath;
	}

	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}

	public boolean isShowLoadingText() {
		return showLoadingText;
	}

	public void setShowLoadingText(boolean showLoadingText) {
		this.showLoadingText = showLoadingText;
	}

	
}
