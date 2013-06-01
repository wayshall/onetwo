package org.onetwo.common.ioc;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public interface InnerContainer extends Container {

	public ObjectInfo getObjectInfo(String key, boolean throwIfNull);

	public ObjectInfo getObjectInfo(Type type);

	public Map<String, ObjectInfo> getObjectInfos(Type type);

	public void setBinder(ObjectBinder binder);

	public ObjectInfoBuilder getInfoBuilder();

	public void setInjectProcessor(InjectProcessor injectProcessor);

	public void setInfoBuilder(ObjectInfoBuilder infoBuilder);
}
