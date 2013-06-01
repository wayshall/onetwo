package org.onetwo.common.ioc;

import java.util.Map;

import org.onetwo.common.ioc.impl.DefaultObjectBinder.MapBinder;

@SuppressWarnings("unchecked")
public interface ObjectBinder {

	public void setContainer(Container container);
	
	public void bind(Class clazz);

	public void bind(String name, String value);

	public void bind(String name, Valuer valuer);

	public void bind(String name, Class clazz, Object... objects);

	public void bindAsList(String name, Object... value);
	
	public Map<String, ObjectInfo> getMapper();

	public void bindAsMap(String name, Object... value);
	
	public void bindAsMap(String name, Map map);

	public MapBinder map(String name);

}