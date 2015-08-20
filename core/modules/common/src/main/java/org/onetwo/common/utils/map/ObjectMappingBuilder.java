package org.onetwo.common.utils.map;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.map.MappableMap.MappingInfo;
import org.onetwo.common.utils.map.MappableMap.MappingValueFunc;

public class ObjectMappingBuilder<S, T> {
	
	public static <S1, T1> ObjectMappingBuilder<S1, T1> newBuilder(Class<S1> srcClass, Class<T1> targetClass){
		return new ObjectMappingBuilder<S1, T1>(srcClass, targetClass);
	}
	
	final private List<MappingInfo<S>> mappingInfos = new ArrayList<>();
	//		final private List<T> sourceObjects;
	private boolean mapAllFields = true;
	@SuppressWarnings("unused")
	private Class<S> srcClass;
	private Class<T> targetClass;
	
	public ObjectMappingBuilder(Class<S> srcClass, Class<T> targetClass) {
		super();
		this.srcClass = srcClass;
		this.targetClass = targetClass;
	}
	public ObjectMappingBuilder<S, T> mapAllFields(){
		this.mapAllFields = true;
		return this;
	}
	public ObjectMappingBuilder<S, T> specifyMappedFields(){
		this.mapAllFields = false;
		return this;
	}
	public ObjectMappingBuilder<S, T> addMapping(String targetFieldName, String srcFieldName){
		this.mappingInfos.add(new MappingInfo<S>(targetFieldName, srcFieldName));
		return this;
	}
	
	public ObjectMappingBuilder<S, T> addMapping(String targetFieldName, MappingValueFunc<S, ?> valueFunc){
		this.mappingInfos.add(new MappingInfo<S>(targetFieldName, valueFunc));
		return this;
	}
	
	
	public List<T> bindValues(List<S> sourceObjects){
	    return sourceObjects.stream().map(obj->bindValue(obj, ReflectUtils.newInstance(targetClass)))
									    .collect(Collectors.toList());
	}
	
	public T bindValue(S sourceObject){
		T mappingObject = ReflectUtils.newInstance(targetClass);
		return bindValue(sourceObject, mappingObject);
	}
	
	public T bindValue(S sourceObject, T mappingObject){
		if(mapAllFields){
			ReflectUtils.copyExcludes(sourceObject, mappingObject);
		}
	    mappingInfos.stream().forEach(maping->{
	    	if(maping.isMappingValueFunc()){
	//			    	mappingObject.put(maping.getJsonFieldName(), maping.getAddMappingValueFunc().mapping(sourceObject));
		    	ReflectUtils.setProperty(mappingObject, maping.getJsonFieldName(), maping.getAddMappingValueFunc().mapping(sourceObject));
	    	}else{
		    	Object value = ReflectUtils.getProperty(sourceObject, maping.getObjectFieldName());
	//			    	mappingObject.put(maping.getJsonFieldName(), value);
		    	ReflectUtils.setProperty(mappingObject, maping.getJsonFieldName(), value);
	    	}
	    });
	    return mappingObject;
	}
		
}
