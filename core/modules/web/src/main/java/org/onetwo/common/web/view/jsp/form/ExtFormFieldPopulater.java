package org.onetwo.common.web.view.jsp.form;


public interface ExtFormFieldPopulater {

	public String getProvider();
	public FormFieldTagBean createTagBean(FormFieldTag tag);
	public void populateFieldComponent(FormFieldTag tag, FormFieldTagBean tagBean);
}
