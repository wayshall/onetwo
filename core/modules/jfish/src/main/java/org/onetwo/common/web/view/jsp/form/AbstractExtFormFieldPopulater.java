package org.onetwo.common.web.view.jsp.form;


abstract public class AbstractExtFormFieldPopulater implements ExtFormFieldPopulater {

	public FormFieldTagBean createTagBean(FormFieldTag tag){
		return FormUIFactory.createUIBean(tag.getFormFieldType());
	}
}
