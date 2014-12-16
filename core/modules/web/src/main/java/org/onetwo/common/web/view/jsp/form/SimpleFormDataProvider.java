package org.onetwo.common.web.view.jsp.form;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
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
	public boolean isReadableProperty(String propertyName) {
		return this.accessor.isReadableProperty(propertyName);
	}

	@Override
	public boolean isFieldShow(String fieldName) {
		return true;
	}

	@Override
	public Object getFieldValue(FormFieldTagBean field) {
		String fieldValue = field.getValue();
		if(StringUtils.isBlank(fieldValue))
			return LangUtils.EMPTY_STRING;

		Object val = "";
		if(field.isModelAttribute()){
			if(this.accessor!=null){
				try {
					if(this.accessor.isReadableProperty(fieldValue))
						val = this.accessor.getPropertyValue(fieldValue);
					else
						val = fieldValue;
				} catch (BeansException e) {
					logger.error("getPropertyValue error : " + e.getMessage());
				}
			}else{
//				val = fieldValue;
			}
		}else{
			val = fieldValue;
		}
		return val;
	}
	
	

}
