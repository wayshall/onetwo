package org.onetwo.common.web.view.jsp.form;

public interface FormDataProvider {

	boolean isReadableProperty(String propertyName);
	public boolean isFieldShow(String fieldName);
	
	public Object getFieldValue(FormFieldTagBean field);

}
