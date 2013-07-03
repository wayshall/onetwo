package org.onetwo.common.utils.propconf;


public interface JFishPropertiesManager<T extends JFishNameValuePair> {
	
	public T getJFishProperty(String name);
	public boolean contains(String fullname);
	public void build();

}