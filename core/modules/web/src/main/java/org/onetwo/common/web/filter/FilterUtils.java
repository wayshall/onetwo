package org.onetwo.common.web.filter;

import org.onetwo.common.profiling.UtilTimerStackObject;

public class FilterUtils {
	private volatile static UtilTimerStackObject instance;
	
	public static UtilTimerStackObject getTimerStackObject(){
		if(instance==null){
			synchronized (FilterUtils.class) {
				if(instance==null){
					instance = UtilTimerStackObject.createObject(); 
				}
			}
		}
		return instance;
	}
	
	

}
