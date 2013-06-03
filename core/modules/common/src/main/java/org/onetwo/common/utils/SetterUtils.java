package org.onetwo.common.utils;

public abstract class SetterUtils {
	
	private static Setter setter = Setter.getInstance();
	
	public static void setBeanProperty(Object bean, String name, Object value){
		setter.setBeanProperty(bean, name, value);
	}
	
}
