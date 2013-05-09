package org.onetwo.common.ioc;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public interface ObjectInfoBuilder {
	public InnerContainer getInnerContainer();
	public void setInnerContainer(InnerContainer innerContainer);
	
	public ObjectInfo build(String name, Object val);
	
	public ObjectInfo buildByList(String name, Object... args);
	

	public ObjectInfo buildByList(String name, List args);

	
	public ObjectInfo buildByMap(String name, Object...args);
	
	public ObjectInfo buildByMap(String name, Map map);

	public ObjectInfo buildByClass(String name, Class clazz, Object... objects);

}