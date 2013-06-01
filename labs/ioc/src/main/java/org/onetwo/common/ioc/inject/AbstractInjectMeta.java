package org.onetwo.common.ioc.inject;

import java.lang.reflect.Type;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.ioc.ObjectInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
abstract public class AbstractInjectMeta implements InjectMeta {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected Class targetClass;
	
	public AbstractInjectMeta(Class targetClass){
		this.targetClass = targetClass;
	}

	public Class getTargetClass() {
		return targetClass;
	}
	
	protected Object getInjectValue(Object bean, ObjectInfo objectInfo){
		Object injectValue = null;
		Type type = getInjectType();
		try {
			if(objectInfo.isProxy() && (type instanceof Class) && !((Class)type).isInterface()){
				injectValue = objectInfo.getActualBean();
				logger.warn("the inject value is a proxy, but the bean["+bean.getClass()+"]'s field["+getName()+"] is not a interface, inject a raw object.");
			}else{
				injectValue = objectInfo.getBean();
			}
		} catch (Exception e) {
			throw new ServiceException("get inject value error : [bean="+bean.getClass()+"], [field="+getName()+"], [message=" + e.getMessage()+"]", e);
		}
		return injectValue;
	}

}
