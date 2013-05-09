package org.onetwo.common.web.s2.ext;

import java.util.Map;

import ognl.MapPropertyAccessor;
import ognl.OgnlException;

import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.conversion.ObjectTypeDeterminer;
import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.ognl.accessor.XWorkMapPropertyAccessor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.reflection.ReflectionContextState;

@SuppressWarnings("unchecked")
public class ExtMapPropertyAccessor extends MapPropertyAccessor {

	private static final Logger LOG = LoggerFactory.getLogger(XWorkMapPropertyAccessor.class);

	private static final String[] INDEX_ACCESS_PROPS = new String[] { "size", "isEmpty", "keys", "values" };

	private XWorkConverter xworkConverter;
	private ObjectFactory objectFactory;
	private ObjectTypeDeterminer objectTypeDeterminer;

	@Inject
	public void setXWorkConverter(XWorkConverter conv) {
		this.xworkConverter = conv;
	}

	@Inject
	public void setObjectFactory(ObjectFactory fac) {
		this.objectFactory = fac;
	}

	@Inject
	public void setObjectTypeDeterminer(ObjectTypeDeterminer ot) {
		this.objectTypeDeterminer = ot;
	}

	@Override
	public Object getProperty(Map context, Object target, Object name) throws OgnlException {
		if(context==null){
			context = OgnlUtils.getDefaultOgnlContext();
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering getProperty (" + context + "," + target + "," + name + ")");
		}

		ReflectionContextState.updateCurrentPropertyPath(context, name);
		// if this is one of the regular index access
		// properties then just let the superclass deal with the
		// get.
		if (name instanceof String && contains(INDEX_ACCESS_PROPS, (String) name)) {
			return super.getProperty(context, target, name);
		}

		Object result = null;

		try {
			result = super.getProperty(context, target, name);
		} catch (ClassCastException ex) {
		}

		if (result == null) {
			// find the key class and convert the name to that class
			Class lastClass = (Class) context.get(XWorkConverter.LAST_BEAN_CLASS_ACCESSED);

			String lastProperty = (String) context.get(XWorkConverter.LAST_BEAN_PROPERTY_ACCESSED);
			if (lastClass == null || lastProperty == null) {
				return super.getProperty(context, target, name);
			}
			Class keyClass = objectTypeDeterminer.getKeyClass(lastClass, lastProperty);

			if (keyClass == null) {

				keyClass = java.lang.String.class;
			}
			Object key = getKey(context, name);
			Map map = (Map) target;
			result = map.get(key);

			if (result == null && context.get(ReflectionContextState.CREATE_NULL_OBJECTS) != null && objectTypeDeterminer.shouldCreateIfNew(lastClass, lastProperty, target, null, false)) {
				Class valueClass = objectTypeDeterminer.getElementClass(lastClass, lastProperty, key);

				if (valueClass == Object.class)
					return null;
				
				try {
					result = objectFactory.buildBean(valueClass, context);
					map.put(key, result);
				} catch (Exception exc) {

				}

			}
		}
		return result;
	}

	/**
	 * @param array
	 * @param name
	 */
	private boolean contains(String[] array, String name) {
		for (String anArray : array) {
			if (anArray.equals(name)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering setProperty(" + context + "," + target + "," + name + "," + value + ")");
		}

		Object key = getKey(context, name);
		Map map = (Map) target;
		map.put(key, getValue(context, value));
	}

	private Object getValue(Map context, Object value) {
		Class lastClass = (Class) context.get(XWorkConverter.LAST_BEAN_CLASS_ACCESSED);
		String lastProperty = (String) context.get(XWorkConverter.LAST_BEAN_PROPERTY_ACCESSED);
		if (lastClass == null || lastProperty == null) {
			return value;
		}
		Class elementClass = objectTypeDeterminer.getElementClass(lastClass, lastProperty, null);
		if (elementClass == null) {
			return value; // nothing is specified, we assume it will be the
							// value passed in.
		}
		return xworkConverter.convertValue(context, value, elementClass);
	}

	private Object getKey(Map context, Object name) {
		Class lastClass = (Class) context.get(XWorkConverter.LAST_BEAN_CLASS_ACCESSED);
		String lastProperty = (String) context.get(XWorkConverter.LAST_BEAN_PROPERTY_ACCESSED);
		if (lastClass == null || lastProperty == null) {
			// return java.lang.String.class;
			// commented out the above -- it makes absolutely no sense for when
			// setting basic maps!
			return name;
		}
		Class keyClass = objectTypeDeterminer.getKeyClass(lastClass, lastProperty);
		if (keyClass == null) {
			keyClass = java.lang.String.class;
		}

		return xworkConverter.convertValue(context, name, keyClass);

	}
}
