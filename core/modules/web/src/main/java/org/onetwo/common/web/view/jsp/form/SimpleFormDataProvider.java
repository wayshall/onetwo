package org.onetwo.common.web.view.jsp.form;

import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessor;

public class SimpleFormDataProvider implements FormDataProvider {

//	private final Object dataObject;
	private PropertyAccessor accessor = null;
	
	public SimpleFormDataProvider(Object dataObject) {
		super();
//		this.dataObject = dataObject;
		if(dataObject!=null){
			BeanWrapper bw = SpringUtils.newBeanWrapper(dataObject);
			this.accessor = bw;
		}
	}

	@Override
	public boolean isFieldShow(String fieldName) {
		return true;
	}

	@Override
	public Object getFieldValue(FormFieldTagBean field) {
		Object val = null;
		if(field.isModelAttribute()){
			if(this.accessor!=null){
				val = this.accessor.getPropertyValue(field.getValue());
			}
		}
		return val;
	}
	
	

}
