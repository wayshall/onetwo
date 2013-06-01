package org.onetwo.common.ioc.impl;

import org.onetwo.common.ioc.Container;
import org.onetwo.common.ioc.ObjectInitialer;
import org.onetwo.common.ioc.Valuer;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class DefaultObjectInitialer implements ObjectInitialer {

	private Container container;
	
	public DefaultObjectInitialer(Container container){
		this.container = container;
	}
	
	@Override
	public Object initializeObject(Object val) {
		Object realVal = null;
		if(String.class.isAssignableFrom(val.getClass())){
			String str = (String) val;
			if(str.indexOf(":")!=-1){
				String[] varArray = StringUtils.split(str, ":");
				String varName = varArray[0];
				String value = varArray[1];
				if("class".equals(varName)){
					realVal = ReflectUtils.newInstance(value);
				}
			}else if(str.startsWith("&")){
				realVal = container.getObject(str.substring(1), true);
			}else{
				realVal = val;
			}
		}else if(Class.class.isAssignableFrom(val.getClass())){
			realVal = ReflectUtils.newInstance((Class)val);
		}else if(Valuer.class.isAssignableFrom(val.getClass())){
			realVal = val;
		}
		else{
//			throw new ServiceException("value must be a class or a valuer : " + val);
			realVal = val;
		}
		return realVal;
	}
	
}
