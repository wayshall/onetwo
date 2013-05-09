package org.onetwo.common.utils.params;

public class ParameterProperty {

	private String name;
	private boolean keep = false;
	
	ParameterProperty(String property){
		setName(property);
	}
	
	public String getName() {
		return name;
	}
	
	public String getKeepName() {
		if(isKeep())
			return Parameter.KEEP+name;
		return name;
	}
	
	public String getTypeName(String type) {
		return type+name;
	}
	
	public void setName(String property) {
		if(property.startsWith(Parameter.KEEP)){
			property = property.substring(Parameter.KEEP.length());
			keep = true;
		}
		this.name = property;
	}
	public boolean isKeep() {
		return keep;
	}
}
