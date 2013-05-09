package org.onetwo.common.ioc.inject.impl;

import java.lang.reflect.Type;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.ioc.InnerContainer;
import org.onetwo.common.ioc.ObjectInfo;
import org.onetwo.common.ioc.annotation.Inject;
import org.onetwo.common.ioc.inject.InjectAnnotationParser;
import org.onetwo.common.ioc.inject.InjectMeta;

@SuppressWarnings("unchecked")
public class InjectParser implements InjectAnnotationParser {

	public boolean containsAnnotation(InjectMeta meta) {
		return meta.getAnnotation(Inject.class) != null;
	}

	public ObjectInfo parse(InjectMeta meta, InnerContainer container) {
		Inject inject = meta.getAnnotation(Inject.class);

		String name = inject.name();
		ObjectInfo val = null;
		Type type = meta.getInjectType();
		if (StringUtils.isNotBlank(name)) {
			val = container.getObjectInfo(name, false);
		} else {
			val = container.getObjectInfo(meta.getName(), false);
			if (val != null && !val.isMatchType(type)) {
				val = null;
			}
			if (val == null) {
				val = container.getObjectInfo(type);
			}
		}
		if (!inject.ignore() && val == null)
			throw new ServiceException("at least one match object[" + meta.getTargetClass() + "] for the field [name=" + meta.getName() + ", type=" + meta.getInjectType() + "]");
		return val;
	}

}
