package org.onetwo.common.web.view.jsp.form;


public interface FormFieldTypePopulater<T extends FormFieldTagBean> {

	public String getFieldType();
	public T createTagBean(FormFieldTag tag);
	public void populateFieldComponent(FormFieldTag tag, T tagBean);
}
