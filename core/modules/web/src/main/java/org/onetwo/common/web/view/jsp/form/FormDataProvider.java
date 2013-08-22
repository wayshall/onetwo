package org.onetwo.common.web.view.jsp.form;

public interface FormDataProvider {
	
	public boolean isFieldShow(String fieldName);
	
	public Object getFieldValue(String value);

}
