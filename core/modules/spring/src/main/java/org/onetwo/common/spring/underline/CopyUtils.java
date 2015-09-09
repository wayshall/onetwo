package org.onetwo.common.spring.underline;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.onetwo.common.reflect.ReflectUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class CopyUtils {
    public static final PropertyDescriptor[] EMPTY_PROPERTIES_ARRAY = new PropertyDescriptor[0];
    
    
    public static class BeanCopierBuilder {
    	public static BeanCopierBuilder newBuilder(){
    		return new BeanCopierBuilder();
    	}
//    	private Object targetObject;
    	private Object fromObject;
    	private PropertyFilter propertyFilter;
    	private PropertyNameConvertor propertyNameConvertor;
    	
    	public BeanCopierBuilder copy(Object from){
    		this.fromObject = from;
    		return this;
    	}

    	public BeanCopierBuilder filter(PropertyFilter propertyFilter){
    		this.propertyFilter = propertyFilter;
    		return this;
    	}

    	public BeanCopierBuilder propertyNameConvertor(PropertyNameConvertor propertyNameConvertor){
    		this.propertyNameConvertor = propertyNameConvertor;
    		return this;
    	}
    	
    	public <T> T to(Class<T> targetClass){
    		T targetObject = ReflectUtils.newInstance(targetClass);
    		to(targetObject);
    		return targetObject;
    	}
    	
    	public <T> void to(T target){
    		SimpleBeanCopier copier = new SimpleBeanCopier();
    		copier.setPropertyFilter(propertyFilter);
    		copier.setPropertyNameConvertor(propertyNameConvertor);
    		copier.fromObject(fromObject, target);
    	}
    }

    public static final PropertyNameConvertor UNDERLINE_CONVERTOR = SimpleBeanCopier.UNDERLINE_CONVERTOR;
    public static final SimpleBeanCopier BEAN_COPIER = new SimpleBeanCopier();
	
	public static BeanWrapper newBeanWrapper(Object obj){
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		bw.setAutoGrowNestedPaths(true);
		return bw;
	}
	
	public static <T> T newInstance(Class<T> targetClass){
		try {
			return targetClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("create instance error: " + e.getMessage(), e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends Collection<?>> T newCollections(Class<?> clazz){
		if(!Collection.class.isAssignableFrom(clazz))
			throw new RuntimeException("class must be a Collection type: " + clazz);
		
		if(clazz==List.class){
			return (T)new ArrayList();
		}else if(clazz==Set.class){
			return (T) new HashSet();
		}else if(clazz==SortedSet.class || clazz==NavigableSet.class){
			return (T) new TreeSet();
		}else if(clazz==Queue.class || clazz==Deque.class){
			return (T) new ArrayDeque();
		}else{
			return (T)newInstance(clazz);
		}
	}
	public static <K, V> Map<K, V> newMap(Class<? extends Map> mapClass){
		if(mapClass==Map.class)
			return new HashMap<K, V>();
		return (Map<K, V>)ReflectUtils.newInstance(mapClass);
	}
	
    public static <T> T copy(Class<T> targetClass, Object src){
    	return copy(newInstance(targetClass), src, UNDERLINE_CONVERTOR);
    }
	

    public static <T> T copy(T target, Object src){
//    	return copy(target, src, UNDERLINE_CONVERTOR);
    	return BEAN_COPIER.fromObject(src, target);
    }
    
    /*****
     * 
     * @param target from src object copy to this target
     * @param src
     * @param convertor
     * @return
     */
    public static <T> T copy(T target, Object src, PropertyNameConvertor convertor){
//    	return new BeanWrappedCopier<T>(target, convertor).fromObject(src);
    	BeanCopierBuilder.newBuilder()
    					.copy(src)
    					.propertyNameConvertor(convertor)
    					.to(target);
    	return target;
    }
    

    public static BeanCopierBuilder copier(){
    	return BeanCopierBuilder.newBuilder();
    }

	public static Collection<String> desribPropertyNames(Class<?> clazz){
		PropertyDescriptor[] props = desribProperties(clazz);
		Set<String> names = new HashSet<String>(props.length);
		for(PropertyDescriptor prop : props){
			names.add(prop.getName());;
		}
		return names;
	}
	
	public static PropertyDescriptor[] desribProperties(Class<?> clazz){
		if(clazz.isInterface())
			return EMPTY_PROPERTIES_ARRAY;
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz, Object.class);
		} catch (Exception e) {
			throw new RuntimeException("get beaninfo error.", e);
		}
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		
		return props;
	}
	
	private CopyUtils(){
	}

}
