package org.onetwo.common.utils;

public class SToken {
	
	private String name;
	private int strIndex;
	
	public SToken(String name, int index) {
		super();
		this.name = name;
		this.strIndex = index;
	}
	
	public boolean isEmptyStr(){
		return StringUtils.isEmpty(name);
	}
	
	public boolean isBlankStr(){
		return StringUtils.isBlank(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStrIndex() {
		return strIndex;
	}

	public void setStrIndex(int strIndex) {
		this.strIndex = strIndex;
	}

	public String toString(){
		return name;
	}
}
