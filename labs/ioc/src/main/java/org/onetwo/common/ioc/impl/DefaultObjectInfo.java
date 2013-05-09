package org.onetwo.common.ioc.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.ioc.JndiUtils;
import org.onetwo.common.ioc.ObjectInfo;
import org.onetwo.common.ioc.Valuer;
import org.onetwo.common.ioc.Container.Destroyable;

@SuppressWarnings("unchecked")
public class DefaultObjectInfo implements ObjectInfo {

	private String name;
	protected Valuer valuer;
	
	private Type[] actualTypes;

	private boolean collection = false;
	private boolean map = false;
	private boolean clazzValue = false;

	public DefaultObjectInfo(String name, Valuer value) {
		super();
		this.name = name;
		this.valuer = value;
		init();
	}
	
	protected void init(){
		Object actualValue = getActualBean();
		Class actualValueType = actualValue.getClass();
		
		if (Collection.class.isAssignableFrom(actualValueType)) {
			collection = true;
			actualTypes = new Type[] { ((Collection) actualValue).iterator().next().getClass() };
		} else if(Map.class.isAssignableFrom(actualValueType)){
			map = true;
			Set<Map.Entry> set = ((Map) actualValue).entrySet();
			Map.Entry e = set.iterator().next();
			actualTypes = new Type[] { e.getKey().getClass(), e.getValue().getClass()};
		}else if (actualValue instanceof Class) {
			clazzValue = true;
			actualTypes = new Type[] { (Class) actualValue };
		} else {
			actualTypes = new Type[] { actualValueType };
		}
	}

	public boolean isCollection() {
		return collection;
	}

	public boolean isMap() {
		return map;
	}

	public boolean isClazzValue() {
		return clazzValue;
	}

	protected boolean isMatchActualValueTypeClass(Class clazz) {
		return clazz.isAssignableFrom((Class) getActualBean().getClass());
	}

	public boolean isMatchType(Type type) {
		boolean match = false;
		if(isCollection()){
			if(!ParameterizedType.class.isAssignableFrom(type.getClass()))
				return false;
		}
		if(isMap()){
			if(!ParameterizedType.class.isAssignableFrom(type.getClass()))
				return false;
		}
		if(isClazzValue()){
			if(!ParameterizedType.class.isAssignableFrom(type.getClass()))
				return false;
		}
		if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
			ParameterizedType ptype = (ParameterizedType) type;
			match = isMatchRawType(ptype.getRawType()) && isMatchActualType(ptype.getActualTypeArguments());
		}else if (Class.class.isAssignableFrom(type.getClass())) {
			match = isMatchActualValueTypeClass((Class) type);
		} else {
			match = this.getActualBean().getClass().equals(type);
		}
		return match;
	}

	protected boolean isMatchRawType(Type type) {
		Class rawTypeClass = null;
		if(ParameterizedType.class.isAssignableFrom(type.getClass())){
			rawTypeClass = (Class)((ParameterizedType)type).getRawType();
		}else{
			rawTypeClass = (Class)type;
		}
		if (Class.class.isAssignableFrom(rawTypeClass.getClass())) {
			return isMatchActualValueTypeClass((Class) type);
		} else {
			return this.getBean().getClass().equals(rawTypeClass);
		}
	}

	protected boolean isMatchActualType(Type[] types) {
		if (actualTypes == types)
			return true;
		if (actualTypes.length != types.length)
			return false;
		Type t = null;
		for (int index=0; index<actualTypes.length; index++) {
			t = actualTypes[index];
//			if (Class.class.isAssignableFrom(t.getClass()) && Class.class.isAssignableFrom(types[index].getClass())) {
			Class supClass = (Class) types[index];
			if (supClass.isAssignableFrom((Class) t))
				continue;
//			}
			if (!t.equals(types[index])) {
				return false;
			}
		}
		return true;
	}

	public String getName() {
		return name;
	}

	public Object getBean() {
		if(Valuer.Type.local.equals(valuer.getType())){
			return JndiUtils.lookup(valuer.getValue().toString());
		}
		return valuer.getValue();
	}

	public Object getActualBean() {
		return valuer.getValue();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("object info: [name=").append(name).append(", valueType=").append(getBean().getClass().toString()).append("]");
		return sb.toString();
	}

	public boolean isProxy() {
		return false;
	}

	protected Valuer getValuer() {
		return valuer;
	}

	public void destroy() {
		this.name = null;
		this.valuer = null;
		if (valuer instanceof Destroyable)
			((Destroyable) valuer).onDestroy();
	}

}
