package org.onetwo.common.web.view.jsp.form;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.onetwo.common.utils.convert.Types;
import org.onetwo.common.web.view.jsp.BaseHtmlTag;
import org.onetwo.common.web.view.jsp.TagUtils;

public class FormFieldTag extends BaseHtmlTag<FormFieldTagBean>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5324408407472195764L;
	private FormFieldType type = FormFieldType.input;
	private boolean showErrorTag = true;
	private String errorPath;
//	private String render;
	private String dataFormat;
	private String value;
	
	//select, checkobx, radio
	private Object items;
	private String itemLabel;
	private String itemValue;

	private boolean readOnly;
	private boolean disabled;
	

	private String permission;
	private boolean showable = true;
	private boolean modelAttribute = true;

	private boolean ignoreField;
	
	
	@Override
	public FormFieldTagBean createComponent() {
		return FormUIFactory.createUIBean(type);
	}
	
	protected FormTagBean getFormTagBean(){
		return getComponentFromRequest(TagUtils.getFormVarName(), FormTagBean.class);
	}

	protected void populateComponent() throws JspException{
		super.populateComponent();
		
		component.setType(type);
		component.setErrorTag(showErrorTag);
		component.setErrorPath(errorPath);
//		component.setRender(render);
		component.setValue(value);
		component.setDataFormat(dataFormat);
		component.setReadOnly(readOnly);
		component.setDisabled(disabled);
		component.setModelAttribute(modelAttribute);
		
		switch (type) {
			case input:
			case password:
			case hidden:
			case textarea:
			case radio:
			case file:
			case checkbox:
				break;
			case select:
			case radioGroup:
			case checkboxGroup:
				populateSelect((FormItemsTagBean)component);
				break;
			case button:
			case submit:
				component.setErrorTag(false);
				break;
	
			default:
				break;
		}
		
//		this.component.buildTagAttributesString();
	}
	
	private void populateSelect(FormItemsTagBean sl){
		sl.setItemLabel(itemLabel);
		sl.setItemValue(itemValue);
		Object itemDatas = null;
		if(String.class.isInstance(items)){
			itemDatas = this.pageContext.getRequest().getAttribute(items.toString());	
		}else{
			itemDatas = this.items;
		}
		sl.setItemDatas(itemDatas);
	}

	private boolean checkIgnoreField(){
		if(!showable)
			return true;
		return !checkPermission(permission);
	}
	
	@Override
	public int doStartTag() throws JspException {
		this.ignoreField = this.checkIgnoreField();
		if(ignoreField)
			return SKIP_BODY;
		
		return super.doStartTag();
	}
	
	@Override
	public int doEndTag() throws JspException {
		if(ignoreField)
			return EVAL_PAGE;
		
		FormTagBean formBean = getFormTagBean();
		if(formBean==null)
			throw new JspException("no parent form tag found for field : " + getName());
		
		if(component.isHtmlTypeRender()){
			BodyContent bc = getBodyContent();
			if(bc!=null)
				component.setBodyContent(bc.getString());
		}
		formBean.addField(component);
		return EVAL_PAGE;
	}


	public void setType(String type) {
		this.type = FormFieldType.valueOf(type);
	}

	public void setErrorTag(String errorTag) {
		this.showErrorTag = Types.convertValue(errorTag, boolean.class);
		this.errorPath = errorTag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setItems(Object items) {
		this.items = items;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public void setShowable(boolean showable) {
		this.showable = showable;
	}

	public void setModelAttribute(boolean modelAttribute) {
		this.modelAttribute = modelAttribute;
	}

}
