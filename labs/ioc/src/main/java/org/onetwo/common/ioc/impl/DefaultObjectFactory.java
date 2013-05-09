package org.onetwo.common.ioc.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.ioc.BFComponentFacotry;
import org.onetwo.common.ioc.BFModule;
import org.onetwo.common.ioc.ContainerAware;
import org.onetwo.common.ioc.InjectProcessor;
import org.onetwo.common.ioc.InnerContainer;
import org.onetwo.common.ioc.ObjectBinder;
import org.onetwo.common.ioc.ObjectInfo;
import org.onetwo.common.ioc.ObjectInfoBuilder;
import org.onetwo.common.ioc.Valuer;
import org.onetwo.common.ioc.annotation.AfterPropertiesSet;
import org.onetwo.common.ioc.proxy.BFInterceptor;
import org.onetwo.common.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings( { "unchecked", "hiding" })
public class DefaultObjectFactory implements InnerContainer {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<String, ObjectInfo> beans = new HashMap<String, ObjectInfo>();
	private InjectProcessor injectProcessor;
	private ObjectBinder binder;
	private ObjectInfoBuilder infoBuilder;

	public DefaultObjectFactory() {
		setBinder(new DefaultObjectBinder());
		setInfoBuilder(BFComponentFacotry.createDefaultObjectInfoBuilder(this));
		setInjectProcessor(new DefaultInjectProcessor());
	}

	public ObjectInfoBuilder getInfoBuilder() {
		return infoBuilder;
	}

	public void setBinder(ObjectBinder binder) {
		this.binder = binder;
		this.binder.setContainer(this);
	}

	public void setInjectProcessor(InjectProcessor injectProcessor) {
		this.injectProcessor = injectProcessor;
		this.injectProcessor.setInnerContainer(this);
	}

	public void setInfoBuilder(ObjectInfoBuilder infoBuilder) {
		this.infoBuilder = infoBuilder;
	}

	public ObjectInfo registerObject(String name, Object val) {
		ObjectInfo info = this.infoBuilder.build(name, val);
		return put(info);
	}

	public void inject(ObjectInfo objectInfo) {
		Object actualObject = null;
		actualObject = objectInfo.getActualBean();
		inject(actualObject);
	}

	@Override
	public void inject(Object object) {
		try {
			awareContainer(object);
			this.injectProcessor.inject(object);
		} catch (Exception e) {
			throw new ServiceException("inject the object["+object.getClass()+"] error: ", e);
		}
		initializingBean(object);
	}

	protected void initializingBean(Object object) {
		if (InitializingBean.class.isAssignableFrom(object.getClass())) {
			try {
				((InitializingBean) object).afterPropertiesSet();
			} catch (Exception e) {
				throw new ServiceException("Initializing bean error : " + object.getClass(), e);
			}
		}
		Method initMethod = ReflectUtils.findUniqueAnnotationMethod(object.getClass(), AfterPropertiesSet.class, true);
		if (initMethod == null)
			return;
		if (initMethod.getParameterTypes() != null && initMethod.getParameterTypes().length > 0)
			throw new ServiceException("the init method[" + initMethod.getName() + "] must can not has parameters!");
		ReflectUtils.invokeMethod(initMethod, object);
	}

	protected void awareContainer(Object object) {
		if (ContainerAware.class.isAssignableFrom(object.getClass()))
			((ContainerAware) object).setContainer(this);
	}

	public ObjectInfo registerList(String name, Object... args) {
		ObjectInfo info = this.infoBuilder.buildByList(name, args);
		return put(info);
	}

	public ObjectInfo registerList(String name, List args) {
		ObjectInfo info = this.infoBuilder.buildByList(name, args);
		return put(info);
	}

	public ObjectInfo registerMap(String name, Map args) {
		ObjectInfo info = this.infoBuilder.buildByMap(name, args);
		return put(info);
	}

	public ObjectInfo registerMap(String name, Object... args) {
		ObjectInfo info = this.infoBuilder.buildByMap(name, args);
		return put(info);
	}

	public ObjectInfo registerClass(String name, Class clazz, Object... objects) {
		ObjectInfo info = this.infoBuilder.buildByClass(name, clazz, objects);
		return put(info);
	}

	public boolean contains(String name) {
		return this.beans.containsKey(name);
	}

	public <T> T getObject(Type clazz) {
		Map<String, Object> maps = getObjects(clazz);
		for (Map.Entry<String, Object> t : maps.entrySet()) {
			return (T) t.getValue();
		}
		return null;
	}

	public Map<String, Object> getObjects(Type type) {
		Map<String, Object> beans = new LinkedHashMap<String, Object>();
		ObjectInfo objInfo = null;
		for (Map.Entry<String, ObjectInfo> entry : this.beans.entrySet()) {
			objInfo = entry.getValue();
			Object bean = null;
			if (objInfo.isMatchType(type)) {
				bean = objInfo.getBean();
				beans.put((String) entry.getKey(), bean);
			}
		}
		return beans;
	}

	public <T> T getObject(String key) {
		return (T) getObject(key, false);
	}

	public <T> T getObject(String key, boolean throwIfNull) {
		ObjectInfo objInfo = beans.get(key);
		if (objInfo == null) {
			if (throwIfNull)
				throw new ServiceException("can not find the object: named[" + key + "]");
			else
				return null;
		}
		return (T) objInfo.getBean();
	}

	public ObjectInfo getObjectInfo(String key, boolean throwIfNull) {
		ObjectInfo objInfo = beans.get(key);
		if (objInfo == null && throwIfNull)
			throw new ServiceException("can not find the objectInfo: named[" + key + "]");
		return objInfo;
	}

	public Map<String, ObjectInfo> getObjectInfos(Type type) {
		Map<String, ObjectInfo> infos = new LinkedHashMap<String, ObjectInfo>();
		ObjectInfo objInfo = null;
		for (Map.Entry<String, ObjectInfo> entry : this.beans.entrySet()) {
			objInfo = entry.getValue();
			if (objInfo.isMatchType(type))
				infos.put((String) entry.getKey(), objInfo);
		}
		return infos;
	}

	public ObjectInfo getObjectInfo(Type type) {
		ObjectInfo objInfo = null;
		for (Map.Entry<String, ObjectInfo> entry : this.beans.entrySet()) {
			objInfo = entry.getValue();
			if (objInfo.isMatchType(type))
				break;
		}
		return objInfo;
	}

	protected ObjectInfo put(ObjectInfo objInfo) {
		beans.put(objInfo.getName(), objInfo);
		return objInfo;
	}

	@Override
	public void build(BFModule... modules) {
		if (modules == null)
			return;
		this.binder.bind(this.getClass().getName(), Valuer.val(this));
		for (BFModule bfm : modules) {
			awareContainer(bfm);
			bfm.build(binder);
		}

		Map<String, ObjectInfo> objectMapper = binder.getMapper();
		if (objectMapper != null) {
			ObjectInfo obj = null;

			for (Map.Entry<String, ObjectInfo> entry : objectMapper.entrySet()) {
				if (logger.isInfoEnabled())
					logger.info("build object: [" + entry.getKey() + " = " + entry.getValue() + "]");
				obj = entry.getValue();
				if (obj.isCollection()) {
					for (Object o : (Collection) obj.getBean())
						inject(o);
				} else if (obj.isMap()) {
					for (Object o : ((Map) obj.getBean()).values()) {
						inject(o);
					}
				} else {
					inject(obj);
				}
			}
		}

		Map<String, Object> bfInterceptors = getObjects(BFInterceptor.class);
		if (bfInterceptors != null) {
			for (Map.Entry entry : bfInterceptors.entrySet()) {
				inject(entry.getValue());
			}
		}
	}

	@Override
	public <T> T getObject(String name, Class<T> clazz) {
		return (T) getObjects(clazz).get(name);
	}

	@Override
	public void destroy() {
		if (beans.isEmpty())
			return;
		Collection<ObjectInfo> objects = beans.values();
		for (ObjectInfo obj : objects) {
			obj.destroy();
			obj = null;
		}
		beans.clear();
	}

}
