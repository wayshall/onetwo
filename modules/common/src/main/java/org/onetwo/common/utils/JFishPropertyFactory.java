package org.onetwo.common.utils;

import org.onetwo.common.profiling.UtilTimerStack;



public final class JFishPropertyFactory {
	
	private static String getActualPropertyName(String propName){
		int arrayIndex = propName.indexOf('['); 
		if(arrayIndex!=-1){
			return propName.substring(0, arrayIndex);
		}else{
			return propName;
		}
	}
	
	public static JFishProperty create(Class<?> beanClass, String exp, boolean isfield){
		String pname = "create JFishProperty";
		UtilTimerStack.push(pname);
		JFishProperty jp = null;
		String[] paths = StringUtils.split(exp, ".");
		Class<?> parentBeanClass = beanClass;
		for(String path : paths){
			path = getActualPropertyName(path);
			if(jp==null){
				if(isfield){
					jp = new JFishFieldInfoImpl(parentBeanClass, path);
				}else{
					jp = new JFishPropertyInfoImpl(parentBeanClass, path);
				}
			}else{
				if(jp.isCollectionType()){
					jp = jp.getFirstParameterTypeClassWrapper().getJFishProperty(path, isfield);
				}else{
					jp = jp.getTypeClassWrapper().getJFishProperty(path, isfield);
				}
			}
		}
		UtilTimerStack.pop(pname);
		return jp;
	}

}
