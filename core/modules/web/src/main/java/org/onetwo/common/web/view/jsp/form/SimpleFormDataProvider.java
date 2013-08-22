package org.onetwo.common.web.view.jsp.form;

import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

public class SimpleFormDataProvider implements FormDataProvider {

//	private final Object dataObject;
	private PropertyAccessor accessor = null;
	
	public SimpleFormDataProvider(Object dataObject) {
		super();
//		this.dataObject = dataObject;
		if(dataObject!=null)
			this.accessor = PropertyAccessorFactory.forBeanPropertyAccess(dataObject);
	}

	@Override
	public boolean isFieldShow(String fieldName) {
		return true;
	}

	@Override
	public Object getFieldValue(String fieldName) {
		Object val = null;
		if(this.accessor!=null)
			val = this.accessor.getPropertyValue(fieldName);
		return val;
	}
	
	

}
