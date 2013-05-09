package org.onetwo.common.ioc.inject.impl;

import java.lang.annotation.Annotation;

import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.ioc.InnerContainer;
import org.onetwo.common.ioc.JndiUtils;
import org.onetwo.common.ioc.ObjectInfo;
import org.onetwo.common.ioc.Valuer;
import org.onetwo.common.ioc.annotation.InjectLocal;
import org.onetwo.common.ioc.inject.InjectAnnotationParser;
import org.onetwo.common.ioc.inject.InjectMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class InjectLocalParser implements InjectAnnotationParser {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected Class<? extends Annotation> annoClass;

	protected boolean cacheLookup = true;

	public InjectLocalParser() {
		this.annoClass = InjectLocal.class;
	}

	public InjectLocalParser(Class<? extends Annotation> annoClass) {
		this.annoClass = annoClass;
	}

	public boolean containsAnnotation(InjectMeta meta) {
		return meta.getAnnotation(annoClass) != null;
	}

	public ObjectInfo parse(InjectMeta meta, InnerContainer container) {
		Class bInterface = getAnnotaionBusinessInterface(meta);
		if (bInterface == null || bInterface == Object.class)
			bInterface = (Class) meta.getInjectType();

		String jndiName = getJndiName(meta, bInterface);
		ObjectInfo objInfo = null;
		try {
			objInfo = lookup(jndiName, bInterface, container);
		} catch (Exception e) {
			String msg = "can not lookup the jndiName: " + jndiName;
			objInfo = (ObjectInfo)handleLookupException(meta, e, msg);
		}
		return objInfo;
	}

	protected ObjectInfo lookupObjectInfo(String name, Object bean, InnerContainer container) {
		ObjectInfo objInfo = null;
		if (cacheLookup)
			objInfo = container.registerObject(name, Valuer.val(bean));
		else
			objInfo = container.getInfoBuilder().build(name, bean);
		return objInfo;
	}

	protected Class getAnnotaionBusinessInterface(InjectMeta field) {
		InjectLocal ejb = field.getAnnotation(InjectLocal.class);
		return ejb.businessInterface();
	}

	protected String getJndiName(InjectMeta field, Class bInterface) {
		InjectLocal ejb = field.getAnnotation(InjectLocal.class);
		String jndiName = ejb.name();
		if (StringUtils.isBlank(jndiName)) {
			jndiName = field.getName();
		}
		if (!jndiName.startsWith(JndiUtils.JAVA_ENV))
			jndiName = JndiUtils.JAVA_ENV + jndiName;
		return jndiName;
	}

	protected ObjectInfo lookup(String name, Class cls, InnerContainer container) throws NamingException {
		ObjectInfo objInfo = null;
		if (cacheLookup) {
			objInfo = container.getObjectInfo(name, false);
			if (objInfo != null)
				return objInfo;
		}
		
		Object bean = JndiUtils.lookup(name);

		if (bean != null)
			objInfo = lookupObjectInfo(name, bean, container);
		return objInfo;
	}

	protected Object handleLookupException(InjectMeta field, Exception e, String msg) {
		InjectLocal ejb = field.getAnnotation(InjectLocal.class);
		if(ejb.ignoreIfLookupError())
			return null;
		logger.error(msg, e);
		throw new ServiceException(msg, e);
	}

}
