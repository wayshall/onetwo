package org.onetwo.common.web.view.jsp.form;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.web.view.jsp.BaseHtmlTag;
import org.onetwo.common.web.view.jsp.TagUtils;

public class FormFieldTag extends BaseHtmlTag<FormFieldTagBean>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5324408407472195764L;
	private FormFieldType type = FormFieldType.input;
	private boolean errorTag = true;
	private String render;
	private String value;
	
	//select, checkobx, radio
	private Object items;
	private String itemLabel;
	private String itemValue;
	
	
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
		component.setErrorTag(errorTag);
		component.setRender(render);
		component.setValue(value);
		
		switch (type) {
			case input:
			case hidden:
			case textarea:
			case radio:
			case checkbox:
				break;
			case select:
			case radioGroup:
			case checkboxGroup:
				populateSelect((FormItemsTagBean)component);
				break;
			case button:
			case submit:
				break;
	
			default:
				throw new BaseException("unspported type: " + type);
		}
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
	
	@Override
	public int doEndTag() throws JspException {
		FormTagBean formBean = getFormTagBean();
		if(formBean==null)
			throw new JspException("no parent form tag found for field : " + getName());
		
		if(component.isHtmlRender()){
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

}
