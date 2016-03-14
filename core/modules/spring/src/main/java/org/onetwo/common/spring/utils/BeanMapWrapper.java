package org.onetwo.common.spring.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.onetwo.common.convert.Types;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.core.convert.TypeDescriptor;


public class BeanMapWrapper extends BeanWrapperImpl implements PropertyAccessor {
	
	private class MapTokens {
		final private String key;
		final private String propertyPath;
		final private int listIndex;
		public MapTokens(String key, String propertyPath) {
			this(key, Integer.MIN_VALUE, propertyPath);
		}
		
		public MapTokens(String key, int listIndex, String propertyPath) {
			super();
			this.key = key;
			this.propertyPath = propertyPath;
			this.listIndex = listIndex;
		}



		public boolean hasPropertyPath(){
			return StringUtils.isNotBlank(propertyPath);
		}

		public boolean isList() {
			return listIndex>=0;
		}

		
	}

//	private BeanWrapper beanWrapper;
	private Map<Object, Object> data;
	private boolean mapData;
	private Map<String, Class<?>> listElementTypes;
	
	public BeanMapWrapper(Object obj, Object...listElementTypes){
		super(obj);
		if(Map.class.isInstance(obj)){
			data = (Map<Object, Object>) obj;
			this.mapData = true;
		}else{
//			beanWrapper = SpringUtils.newBeanWrapper(obj);
		}
		if(LangUtils.isEmpty(listElementTypes)){
			this.listElementTypes = Collections.EMPTY_MAP;
		}else{
			this.listElementTypes = LangUtils.asMap(listElementTypes);
		}
		setAutoGrowNestedPaths(true);
	}
	
	private MapTokens parseMapExp(String exp){
		char[] chars = exp.toCharArray();
		char ch;
		MapTokens tokens = null;
		for(int i=0; i<chars.length; i++){
			ch = chars[i];
			if(ch=='.'){
				tokens = new MapTokens(exp.substring(0, i), exp.substring(i+1));
				break;
			}else if(ch=='['){
				String indexStr = null;
				int end = i+1;
				for(; end<chars.length; end++){
					if(chars[end]==']'){
						indexStr = exp.substring(i+1, end);
						break;
					}
				}
				if(StringUtils.isNotBlank(indexStr)){
					int listIndex = Types.convertValue(indexStr, int.class);
					if(end<chars.length-1 && chars[end+1]=='.')
						end = end+2;//user[0].xxx
					else
						end = end+1;
					tokens = new MapTokens(exp.substring(0, i), listIndex, exp.substring(end));
				}else{
					tokens = new MapTokens(exp.substring(0, i), exp.substring(i));
				}
				break;
			}
		}
		if(tokens==null){
			tokens = new MapTokens(exp, null);
		}
		return tokens;
		/*int dotIndex = StringUtils.indexOfAny(exp, ".", "[");
		if(dotIndex!=-1){
			return new MapTokens(exp.substring(0, dotIndex), exp.substring(dotIndex+1));
		}else{
			return new MapTokens(exp, null);
		}*/
	}
	
	public void setPropertyValue(String propertyName, Object value) throws BeansException {
		if(mapData){
			MapTokens token = parseMapExp(propertyName);
			if(token.isList()){
				List<Object> propValue = (List<Object>)data.get(token.key);
				if(propValue==null && isAutoGrowNestedPaths()){
					propValue = LangUtils.newArrayList();
					data.put(token.key, propValue);
				}

				initList(propValue, token.listIndex);
				
				if(token.hasPropertyPath()){
					Object ele = propValue.get(token.listIndex);
					if(ele==null){
						Class<?> eType = ReflectUtils.getGenricType(propValue, 0);
						ele = this.createListElement(token, eType);
						propValue.set(token.listIndex, ele);
					}
					
					BeanWrapper bw = newBeanWrapper(ele);
					bw.setPropertyValue(token.propertyPath, value);
				}else{
					propValue.set(token.listIndex, value);
				}
			}else if(token.hasPropertyPath()){
				Object rs = data.get(token.key);
				BeanWrapper bw = newBeanWrapper(rs);
				bw.setPropertyValue(token.propertyPath, value);
			}else{
				data.put(propertyName, value);
			}
		}else{
			super.setPropertyValue(propertyName, value);
		}
	}
	
	/*private void setListValue(List<Object> list, int index, Object ele){
		initList(list, index);
		list.set(index, ele);
	}*/
	private void initList(List<?> list, int untilIndex){
		int lsize = untilIndex+1;
		if(list.size()>=lsize)
			return ;
		lsize = lsize - list.size();
		for (int i = 0; i < lsize; i++) {
			list.add(null);
		}
	}

	@Override
	public Object getPropertyValue(String propertyName) throws BeansException {
		if(mapData){
			MapTokens token = parseMapExp(propertyName);
			if(token.isList()){
				List<Object> value = (List<Object>)data.get(token.key);
				Object indexValue = value.get(token.listIndex);
				if(token.hasPropertyPath()){
					BeanWrapper bw = newBeanWrapper(indexValue);
					return bw.getPropertyValue(token.propertyPath);
				}
				return indexValue;
			}else if(token.hasPropertyPath()){
				Object rs = data.get(token.key);
				BeanWrapper bw = newBeanWrapper(rs);
				return bw.getPropertyValue(token.propertyPath);
			}else{
				return data.get(propertyName);
			}
		}else{
			return super.getPropertyValue(propertyName);
		}
	}

	private Object createListElement(MapTokens token, Class<?> eType){
		if(!LangUtils.isSimpleType(eType) && eType==Object.class){
			if(this.listElementTypes.containsKey(token.key)){
				eType = this.listElementTypes.get(token.key);
			}else{
				throw new BaseException("the type of list element is unknow: " + eType);
			}
			Object e = ReflectUtils.newInstance(eType);
			return e;
		}
		return null;
	}

	@Override
	public boolean isReadableProperty(String propertyName) {
		if(mapData){
			MapTokens token = parseMapExp(propertyName);
			if(token.isList()){
				List<Object> propValue = (List<Object>)data.get(token.key);
				if(propValue==null)
					return data.containsKey(token.key);
				
				if(token.hasPropertyPath()){
					Class<?> eType = ReflectUtils.getGenricType(propValue, 0);
					Object ele = this.createListElement(token, eType);
					BeanWrapper bw = newBeanWrapper(ele);
					return bw.isReadableProperty(token.propertyPath);
				}
				
				return true;
			}else if(token.hasPropertyPath()){
				Object rs = data.get(token.key);
				if(rs==null)
					return data.containsKey(token.key);
				
//				Class<?> eType = ReflectUtils.getGenricType(rs, 0);
//				Object ele = this.createListElement(token, eType);
				BeanWrapper bw = newBeanWrapper(rs);
				return bw.isReadableProperty(token.propertyPath);
			}else{
				return data.containsKey(propertyName);
			}
		}else{
			return super.isReadableProperty(propertyName);
		}
	}

	protected BeanWrapper newBeanWrapper(Object obj){
		return SpringUtils.newBeanMapWrapper(obj);
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
