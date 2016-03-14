package org.onetwo.common.spring.underline;

import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.TypeMismatchException;

public class UnderlineBeanWrapper extends BeanWrapperImpl {
	
	private boolean needTransformPropertyName;
	private char spliter = '_';
	
	public UnderlineBeanWrapper() {
		super();
	}

	public UnderlineBeanWrapper(boolean registerDefaultEditors) {
		super(registerDefaultEditors);
	}

	public UnderlineBeanWrapper(Class<?> clazz) {
		super(clazz);
	}

	public UnderlineBeanWrapper(Object object, String nestedPath, Object rootObject) {
		super(object, nestedPath, rootObject);
	}

	public UnderlineBeanWrapper(Object object) {
		super(object);
	}

	protected String transformPropertyName(String propertyName){
		if(needTransformPropertyName && propertyName.indexOf(spliter)!=-1){
			return StringUtils.toCamel(propertyName, spliter, false);
		}
		return propertyName;
	}
	
	protected void setIntrospectionClass(Class<?> clazz) {
		super.setIntrospectionClass(clazz);
		this.needTransformPropertyName = clazz.getAnnotation(ConvertUnderlineProperty.class)!=null;
	}

	@Override
	public Object getPropertyValue(String propertyName) throws BeansException {
		return super.getPropertyValue(transformPropertyName(propertyName));
	}
	@Override
	public void setPropertyValue(String propertyName, Object value)
			throws BeansException {
		super.setPropertyValue(transformPropertyName(propertyName), value);
	}

	protected Class<?> guessPropertyTypeFromEditors(String propertyName) {
		return super.guessPropertyTypeFromEditors(transformPropertyName(propertyName));
	}
	
	@Override
	public Object convertForProperty(Object value, String propertyName)
			throws TypeMismatchException {
		return super.convertForProperty(value, transformPropertyName(propertyName));
	}
	
	/*@Override
	public PropertyDescriptor getPropertyDescriptor(String propertyName)
			throws BeansException {
		// TODO Auto-generated method stub
		return super.getPropertyDescriptor(propertyName);
	}*/

	public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown, boolean ignoreInvalid) throws BeansException {
		if(!needTransformPropertyName){
			super.setPropertyValues(pvs, ignoreUnknown, ignoreInvalid);
			return ;
		}
		MutablePropertyValues mpvs = convertMutablePropertyValues((MutablePropertyValues) pvs);
		super.setPropertyValues(mpvs, ignoreUnknown, ignoreInvalid);
	}
	
	protected MutablePropertyValues convertMutablePropertyValues(MutablePropertyValues mpvs){
		MutablePropertyValues newMpvs = new MutablePropertyValues();
		for(PropertyValue pv : mpvs.getPropertyValueList()){
			String propertyName = transformPropertyName(pv.getName());
			newMpvs.addPropertyValue(propertyName, pv.getValue());
		}
		return newMpvs;
	}

	protected UnderlineBeanWrapper newNestedBeanWrapper(Object object, String nestedPath) {
		return new UnderlineBeanWrapper(object, nestedPath, this);
	}

}
