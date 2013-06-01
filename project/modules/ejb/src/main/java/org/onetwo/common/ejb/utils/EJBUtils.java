package org.onetwo.common.ejb.utils;

import org.onetwo.common.ejb.exception.AppEJBException;

public abstract class EJBUtils {
	
	public static void throwAppEJBException(Exception e){
		throw new AppEJBException(e);
	}
	public static void throwAppEJBException(String msg){
		throw new AppEJBException(msg);
	}
	public static void throwAppEJBException(String msg, Exception e){
		throw new AppEJBException(msg, e);
	}
	
	public static void throwIfNull(Object value, String msg){
		if(value==null)
			throwAppEJBException(msg);
	}

}
