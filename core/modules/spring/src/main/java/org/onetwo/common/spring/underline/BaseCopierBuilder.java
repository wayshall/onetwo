package org.onetwo.common.spring.underline;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.ArrayUtils;



@SuppressWarnings("unchecked")
abstract public class BaseCopierBuilder<B extends BaseCopierBuilder<B>> {
	
	private CompositePropertyFilter propertyFilters =  new CompositePropertyFilter();
	private PropertyNameConvertor propertyNameConvertor;

	/****
	 * 添加filter
	 * 最终会组合这些filter，所有filter都通过才会被copy
	 * @param propertyFilter
	 * @return
	 */
	public B filter(PropertyFilter propertyFilter){
//		this.checkPropertyFilterNotNull();
		this.propertyFilters.add(propertyFilter);
		return (B)this;
	}

	public B ignoreNullValue(){
		return filter(SimplePropertyFilters.IGNORE_NULL);
	}

	public B ignoreBlankString(){
		return filter(SimplePropertyFilters.IGNORE_BLANK_STRING);
	}

	public B ignoreFields(String...fieldNames){
		return filter((prop, fromValue)->{
			return ArrayUtils.contains(fieldNames, prop.getName());
		});
	}

	public B propertyNameConvertor(PropertyNameConvertor propertyNameConvertor){
		this.checkPropertyNameConvertorNotNull();
		this.propertyNameConvertor = propertyNameConvertor;
		return (B)this;
	}

	public B nameWithSeperator(String seperator){
		this.checkPropertyNameConvertorNotNull();
		this.propertyNameConvertor = new SeperatorNamedConvertor(seperator);
		return (B)this;
	}
	
	private void checkPropertyNameConvertorNotNull(){
		if(this.propertyNameConvertor!=null)
			throw new BaseException("propertyNameConvertor is not null, you can't override it a not null PropertyNameConvertor!");
	}
	
	/*private void checkPropertyFilterNotNull(){
		if(this.propertyFilter!=null)
			throw new BaseException("propertyFilter is not null, you can't override it a not null propertyFilter!");
	}*/

	public B nameWithUnderline(){
		this.checkPropertyNameConvertorNotNull();
		this.propertyNameConvertor = SeperatorNamedConvertor.UNDERLINE_CONVERTOR;
		return (B)this;
	}
	
	protected SimpleBeanCopier newCopier(){
		SimpleBeanCopier copier = new SimpleBeanCopier();
		copier.setPropertyFilter(this.propertyFilters);
		copier.setPropertyNameConvertor(getPropertyNameConvertor());
		return copier;
	}
	
	/*
	protected PropertyFilter getPropertyFilter() {
		return propertyFilter;
	}*/

	protected PropertyNameConvertor getPropertyNameConvertor() {
		return propertyNameConvertor;
	}

	public static class SimpleCopierBuilder<T, B extends SimpleCopierBuilder<T, B>> extends BaseCopierBuilder<SimpleCopierBuilder<T, B>>{
		
		private T fromObject;

		public B copy(T from){
			this.fromObject = from;
			return (B)this;
		}
		/*public T to(Class<T> targetClass){
			T targetObject = ReflectUtils.newInstance(targetClass);
			to(targetObject);
			return targetObject;
		}*/
		public T toNewInstance(){
			T targetObject = (T)ReflectUtils.newInstance(fromObject.getClass());
			to(targetObject);
			return targetObject;
		}
		public <E> E toClass(Class<E> targetClass){
			E targetObject = ReflectUtils.newInstance(targetClass);
			newCopier().fromObject(fromObject, targetObject);
			return targetObject;
		}
		
		public void to(T target){
			newCopier().fromObject(fromObject, target);
		}

	}

}
