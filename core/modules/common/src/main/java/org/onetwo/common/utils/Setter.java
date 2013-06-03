package org.onetwo.common.utils;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;
import org.onetwo.common.exception.ServiceException;

public class Setter {
	
	private static Setter instance = new Setter();
	
	public static Setter getInstance() {
		return instance;
	}

	private Setter(){
	}

	public void setBeanProperty(Object bean, String name, Object value){
		Object parentObj = bean;
		try {
			String[] paths = StringUtils.split(name, ".");
			String targetProperty = name;
			int index = 0;
			for(String path : paths){
				if(index==(paths.length-1)){
					targetProperty = path;
					break;
				}
				Object pathObj = PropertyUtils.getProperty(parentObj, path);
				if(pathObj==null){
					PropertyDescriptor pd = ReflectUtils.getPropertyDescriptor(parentObj, path);
					if(pd!=null){
						pathObj = ReflectUtils.newInstance(pd.getPropertyType());
						PropertyUtils.setProperty(parentObj, path, pathObj);
					}
				}
				if(pathObj!=null)
					parentObj = pathObj;
				index++;
			}
			PropertyUtils.setProperty(parentObj, targetProperty, value);
		} catch (Exception e) {
			throw new ServiceException("setBeanProperty error : class["+bean+"], property[" + name+"]", e);
		}
	}
}
