package org.onetwo.common.ioc;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


@SuppressWarnings({"unchecked","hiding"})
public interface Container {

	public void build(BFModule...modules);

	public void inject(Object object);
	

	public interface InitializingBean{
		
		public void afterPropertiesSet() throws Exception ;
		
	}
	
	public interface Destroyable {
		
		public void onDestroy();
		
	}
	
	public ObjectInfo registerObject(String name, Object val);

	public ObjectInfo registerList(String name, Object...objs);
	
	public ObjectInfo registerList(String name, List args);

	public ObjectInfo registerMap(String name, Map args);

	public ObjectInfo registerMap(String name, Object...args);

	public ObjectInfo registerClass(String name, Class clazz, Object... objects);
	

	public boolean contains(String name);

	public Map<String, Object> getObjects(Type type);
	
	public <T> T getObject(Type clazz);
	
	public <T> T getObject(String key);
	public <T> T getObject(String key, boolean throwIfNull);
	
	public <T>T getObject(String name, Class<T> clazz);
	

	public void destroy();
}
