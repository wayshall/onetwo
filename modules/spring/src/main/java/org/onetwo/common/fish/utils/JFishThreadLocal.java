package org.onetwo.common.fish.utils;

public class JFishThreadLocal<T> extends ThreadLocal<T>{
	
	private final String name;

	public JFishThreadLocal(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public String toString(){
		return name;
	}

}
