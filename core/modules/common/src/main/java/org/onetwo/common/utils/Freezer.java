package org.onetwo.common.utils;

import java.io.Serializable;

public class Freezer implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8530734955693787392L;
	
	public static Freezer create(Class<?> clazz){
		return new Freezer(clazz.getName());
	}
	
	private String target;
	private boolean freezing;
	
	public Freezer(String target){
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void freezing(){
		this.freezing = true;
	}

	/*public void unFreezing(){
		this.freezing = false;
	}*/

	public boolean isFreezing(){
		return freezing;
	}
	
	public void checkOperation(String name){
		if(freezing){
			throw new UnsupportedOperationException("this target["+this.target+"] is freezing, the operation["+name+"] is failed!");
		}
	}
	
	public void checkOperation(){
		if(freezing){
			StackTraceElement[] sts = Thread.currentThread().getStackTrace();
			if(LangUtils.size(sts)>=2){
				String name = Thread.currentThread().getStackTrace()[1].getMethodName();
				throw new UnsupportedOperationException("this target["+this.target+"] is freezing, the operation["+name+"] is failed!");
			}else{
				throw new UnsupportedOperationException("this target["+this.target+"] is freezing, the operation is failed!");
			}
		}
	}
}
