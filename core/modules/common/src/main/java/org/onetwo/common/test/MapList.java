package org.onetwo.common.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.Assert;

import com.google.common.collect.Maps;

@SuppressWarnings("serial")
public class MapList<E> extends ArrayList<E> {
	/**
	 * 
	 */
	private Map<Object, E> dataMap = Maps.newHashMap();

	@Override
	public boolean add(E e) {
		Object id = ReflectUtils.getPropertyValue(e, "id");
		dataMap.put(id, e);
		return super.add(e);
	}
	
	public E findById(Object id){
		return dataMap.get(id);
	}
	
	public List<E> findList(String propName, Object value){
		Assert.notNull(value);
		return this.stream().filter(e->{
			Object fieldValue = ReflectUtils.getPropertyValue(e, propName);
			return value.equals(fieldValue);
		})
		.collect(Collectors.toList());
	}
	
	public E findOne(String propName, Object value){
		Assert.notNull(value);
		return this.stream().filter(e->{
			Object fieldValue = ReflectUtils.getPropertyValue(e, propName);
			return value.equals(fieldValue);
		})
		.findFirst()
		.orElse(null);
	}
	

}
