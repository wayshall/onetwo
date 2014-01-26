package org.onetwo.common.web.view.jsp.form;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessor;

public class SimpleFormDataProvider implements FormDataProvider {

	private static final Logger logger = MyLoggerFactory.getLogger(SimpleFormDataProvider.class);
	
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
		Object val = field.getValue();
		if(field.isModelAttribute()){
			if(this.accessor!=null){
				try {
					if(this.accessor.isReadableProperty(field.getValue()))
						val = this.accessor.getPropertyValue(field.getValue());
				} catch (BeansException e) {
					logger.error("getPropertyValue error : " + e.getMessage());
				}
			}
		}
		return val;
	}
	
	

}
