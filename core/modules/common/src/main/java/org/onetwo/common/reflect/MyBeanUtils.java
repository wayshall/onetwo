package org.onetwo.common.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.expression.Resolver;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/****
 * 对bean进行操作的实用类
 * 和beanUtils类似
 * 部分代码copy自apache的BeanUtils
 * 已废弃，建议使用copyUtils
 * 
 * @author weishao
 *
 */
@Deprecated
@SuppressWarnings({ "unchecked"})
public class MyBeanUtils {

	private static MyBeanUtilsBean BeanUtils = new MyBeanUtilsBean();
	private static MyBeanUtilsBean IgnoreCollectionBeanUtils = new MyBeanUtilsBean(true);

	public static Object getProperty(Object bean, String property){
		Object val = null;
		try {
			val = BeanUtils.getProperty(bean, property);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public static void setProperty(Object bean, String name, Object value){
		try {
			BeanUtils.setProperty(bean, name, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void copyProperties(Object dest, Object orig) {
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			throw new RuntimeException("copy properties error!", e);
		}
	}

	public static void copyPropertiesIgnoreCollection(Object dest, Object orig) {
		try {
			IgnoreCollectionBeanUtils.copyProperties(dest, orig);
		} catch (Exception e) {
			throw new RuntimeException("copy properties error!", e);
		}
	}

	/*public static Object convertValue(Object val, Class targetClass){
		if(val==null || StringUtils.isBlank(val.toString()))
			return val;
		Object value = null;
		if(val.getClass().isArray()){
			Object[] array = (Object[]) val;
			if(array.length==0)
				return null;
			StringBuilder sb = new StringBuilder("|");
			for(Object v : array){
				sb.append(v.toString());
				sb.append("|");
			}
			value = sb.toString();
		}else{
			value = OgnlOps.convertValue(val, targetClass);
		}
		return value;
	}
	*/
	public static <T> T copyBean(Object source, Class<T> targetClass){
		T target = ReflectUtils.newInstance(targetClass);
		copyPropertiesIgnoreCollection(target, source);
		return target;
	}
	
	public static List copyList(List sources, Class targetClass){
		if(sources==null)
			return null;
		
		List results = new ArrayList();
		for(Object s : sources){
			Object bean = copyBean(s, targetClass);
			if(bean!=null)
				results.add(bean);
		}
		
		return results;
	}

}

@SuppressWarnings("unchecked")
class MyBeanUtilsBean extends BeanUtilsBean {
	private static final List<Class<?>> BASE_CLASS;

	static {
		List<Class<?>> cls = new ArrayList<Class<?>>();
		cls.add(Boolean.class);
		cls.add(boolean.class);
		cls.add(Character.class);
		cls.add(char.class);
		cls.add(Byte.class);
		cls.add(byte.class);
		cls.add(Short.class);
		cls.add(short.class);
		cls.add(Integer.class);
		cls.add(int.class);
		cls.add(Long.class);
		cls.add(long.class);
		cls.add(Float.class);
		cls.add(float.class);
		cls.add(Double.class);
		cls.add(double.class);
		cls.add(String.class);
		cls.add(Date.class);
		cls.add(Number.class);
		cls.add(Collection.class);
		BASE_CLASS = Collections.unmodifiableList(cls);
	}

	private Logger log = LoggerFactory.getLogger(MyBeanUtilsBean.class);
	private boolean isIgnoreCollection = false;

	public MyBeanUtilsBean() {
	}

	public MyBeanUtilsBean(boolean isIgnoreCollection) {
		this.isIgnoreCollection = isIgnoreCollection;
	}

	protected boolean isAllowCopyType(Class<?> clazz) {
		boolean allow = BASE_CLASS.contains(clazz);
		if(allow==true)
			return allow;
		Class<?> sup = clazz.getSuperclass();
		if (sup != null && sup != Object.class)
			allow = this.isAllowCopyType(sup);
		return allow;
	}

	public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {

		if (value == null)
			return;
		if(value instanceof String && StringUtils.isBlank(value.toString()))
			return ;
		
		Object target = bean;
		Resolver resolver = getPropertyUtils().getResolver();
		while (resolver.hasNested(name)) {
			try {
				target = getPropertyUtils().getProperty(target, resolver.next(name));
				name = resolver.remove(name);
			} catch (NoSuchMethodException e) {
				return; // Skip this property setter
			}
		}

		String propName = resolver.getProperty(name); // Simple name of target

		Class type = null; // Java type of target property
		int index = resolver.getIndex(name); // Indexed subscript value (if
		// any)
		String key = resolver.getKey(name); // Mapped key value (if any)

		// Calculate the target property type
		if (target instanceof DynaBean) {
			DynaClass dynaClass = ((DynaBean) target).getDynaClass();
			DynaProperty dynaProperty = dynaClass.getDynaProperty(propName);
			if (dynaProperty == null) {
				return; // Skip this property setter
			}
			type = dynaProperty.getType();
		} else {
			PropertyDescriptor descriptor = null;
			try {
				descriptor = getPropertyUtils().getPropertyDescriptor(target, name);
				if (descriptor == null) {
					return; // Skip this property setter
				}
			} catch (NoSuchMethodException e) {
				return; // Skip this property setter
			}
			type = descriptor.getPropertyType();
			if (type == null) {
				// Most likely an indexed setter on a POJB only
				if (log.isTraceEnabled()) {
					log.trace("    target type for property '" + propName + "' is null, so skipping ths setter");
				}
				return;
			}
		}
		if (log.isTraceEnabled()) {
			log.trace("    target propName=" + propName + ", type=" + type + ", index=" + index + ", key=" + key);
		}

		// Convert the specified value to the required type and store it
		if (index >= 0) { // Destination must be indexed
			value = convert(value, type.getComponentType());
			try {
				getPropertyUtils().setIndexedProperty(target, propName, index, value);
			} catch (NoSuchMethodException e) {
				throw new InvocationTargetException(e, "Cannot set " + propName);
			}
		} else if (key != null) { // Destination must be mapped
			// Maps do not know what the preferred data type is,
			// so perform no conversions at all
			try {
				getPropertyUtils().setMappedProperty(target, propName, key, value);
			} catch (NoSuchMethodException e) {
				throw new InvocationTargetException(e, "Cannot set " + propName);
			}
		} else { // Destination must be simple
			value = convert(value, type);
			try {
				if (this.isAllowCopyType(type)) {
					if (!(value instanceof Collection) || (!this.isIgnoreCollection && !((Collection) value).isEmpty()))
						getPropertyUtils().setSimpleProperty(target, propName, value);
				}
			} catch (NoSuchMethodException e) {
				throw new InvocationTargetException(e, "Cannot set " + propName);
			}
		}

	}
	
}