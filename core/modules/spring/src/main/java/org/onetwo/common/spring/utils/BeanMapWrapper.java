package org.onetwo.common.spring.utils;

import java.util.Map;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.core.convert.TypeDescriptor;

public class BeanMapWrapper extends BeanWrapperImpl implements PropertyAccessor {
	
	private class MapTokens {
		final private String key;
		final private String propertyPath;
		public MapTokens(String key, String propertyPath) {
			super();
			this.key = key;
			this.propertyPath = propertyPath;
		}
		
		public boolean hasPropertyPath(){
			return StringUtils.isNotBlank(propertyPath);
		}
		
	}

//	private BeanWrapper beanWrapper;
	private Map<Object, Object> data;
	private boolean mapData;
	
	public BeanMapWrapper(Object obj){
		super(obj);
		if(Map.class.isInstance(obj)){
			data = (Map<Object, Object>) obj;
			this.mapData = true;
		}else{
//			beanWrapper = SpringUtils.newBeanWrapper(obj);
		}
		setAutoGrowNestedPaths(true);
	}
	
	private MapTokens parseMapExp(String exp){
		int dotIndex = exp.indexOf('.');
		if(dotIndex!=-1){
			return new MapTokens(exp.substring(0, dotIndex), exp.substring(dotIndex+1));
		}else{
			return new MapTokens(exp, null);
		}
	}

	public void setPropertyValue(String propertyName, Object value) throws BeansException {
		if(mapData){
			data.put(propertyName, value);
		}else{
			super.setPropertyValue(propertyName, value);
		}
	}
	
	public Object getPropertyValue(String propertyName) throws BeansException {
		if(mapData){
			MapTokens token = parseMapExp(propertyName);
			if(token.hasPropertyPath()){
				Object value = data.get(token.key);
				BeanWrapper bw = SpringUtils.newBeanWrapper(value);
				return bw.getPropertyValue(token.propertyPath);
			}else{
				return data.get(propertyName);
			}
		}else{
			return super.getPropertyValue(propertyName);
		}
	}


	@Override
	public boolean isReadableProperty(String propertyName) {
		if(mapData){
			return true;
		}else{
			return super.isReadableProperty(propertyName);
		}
	}


	@Override
	public boolean isWritableProperty(String propertyName) {
		if(mapData){
			return true;
		}else{
			return super.isWritableProperty(propertyName);
		}
	}


	@Override
	public Class getPropertyType(String propertyName) throws BeansException {
		if(mapData){
			return null;
		}else{
			return super.getPropertyType(propertyName);
		}
	}


	@Override
	public TypeDescriptor getPropertyTypeDescriptor(String propertyName)
			throws BeansException {
		if(mapData){
			return null;
		}else{
			return super.getPropertyTypeDescriptor(propertyName);
		}
	}


	@Override
	public void setPropertyValues(Map<?, ?> map) throws BeansException {
		if(mapData){
			data.putAll(map);
		}else{
			super.setPropertyValues(map);
		}
		
	}


	@Override
	public void setPropertyValues(PropertyValues pvs) throws BeansException {
		if(mapData){
			throw new UnsupportedOperationException();
		}else{
			super.setPropertyValues(pvs);
		}
		
	}
	@Override
	public void setPropertyValue(PropertyValue pv) throws BeansException {
		if(mapData){
			throw new UnsupportedOperationException();
		}else{
			super.setPropertyValue(pv);
		}
	}


	@Override
	public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown)
			throws BeansException {
		if(mapData){
			throw new UnsupportedOperationException();
		}else{
			super.setPropertyValues(pvs, ignoreUnknown);
		}
		
	}


	@Override
	public void setPropertyValues(PropertyValues pvs, boolean ignoreUnknown,
			boolean ignoreInvalid) throws BeansException {
		if(mapData){
			throw new UnsupportedOperationException();
		}else{
			super.setPropertyValues(pvs, ignoreUnknown, ignoreInvalid);
		}
		
	}


	

}
