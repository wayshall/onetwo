package org.onetwo.common.fish.orm;

public class Propertable {
 
	protected String javaName;
	
	protected Class<?> javaType;

	public String getJavaName() {
		return javaName;
	}

	public Class<?> getJavaType() {
		return javaType; 
	}

	public void setJavaName(String javaName) {
		this.javaName = javaName;
	}

	public void setJavaType(Class<?> javaType) {
		this.javaType = javaType;
	}

	public String getReadMethodName() {
		String prefix = "get";
		String name = this.getJavaName();
		if(javaType==Boolean.class){
			prefix = "is";
			if(name.startsWith(prefix))
				name = name.substring(prefix.length());
		}
		name = name.substring(0, 1).toUpperCase()+this.getJavaName().substring(1);
		return prefix+name;
	}

	public String getWriteMethodName() {
		String prefix = "set";
		String name = this.getJavaName().substring(0, 1).toUpperCase()+this.getJavaName().substring(1);
		return prefix+name;
	}
	
}
