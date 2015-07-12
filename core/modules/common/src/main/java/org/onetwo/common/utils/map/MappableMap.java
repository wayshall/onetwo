package org.onetwo.common.utils.map;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.utils.ReflectUtils;

/***
 * 把对象映射为map
 * @author way
 *
 */
@SuppressWarnings("serial")
public class MappableMap extends HashMap<String, Object>{
	
	public static <E> StaticMappingBuilder<E> newMappingBuilder(){
		return new StaticMappingBuilder<E>();
	}
	
//	final private Object sourceObject;
//	final private List<MappingInfo> mappingInfos = new ArrayList<>();

	public MappableMap() {
	    super();
    }
	
	public MappableMap from(Object sourceObject){
	    PropertyDescriptor[] props = ReflectUtils.desribProperties(sourceObject.getClass());
	    Stream.of(props).forEach(p->{
	    	Object value = ReflectUtils.getProperty(sourceObject, p);
	    	if(value==null)
	    		return ;
	    	super.put(p.getName(), value);
	    });
	    return this;
	}
	
	public <E> MappingBuilder<E> mapFrom(Object sourceObject){
	    return new MappingBuilder<E>(this, sourceObject);
	}
	
	public static class StaticMappingBuilder<T> {
		final private List<MappingInfo<T>> mappingInfos = new ArrayList<>();
//		final private List<T> sourceObjects;
		public StaticMappingBuilder() {
//	        this.sourceObjects = sourceObjects;//List<T> sourceObjects
        }
		
		public StaticMappingBuilder<T> addMapping(String jsonFieldName, String objectFieldName){
			this.mappingInfos.add(new MappingInfo<T>(jsonFieldName, objectFieldName));
			return this;
		}
		
		public StaticMappingBuilder<T> addMapping(String jsonFieldName, MappingValueFunc<T, ?> valueFunc){
			this.mappingInfos.add(new MappingInfo<T>(jsonFieldName, valueFunc));
			return this;
		}

		
		public List<MappableMap> bindValues(List<T> sourceObjects){
		    return bindValues(true, sourceObjects);
		}
		
		public List<MappableMap> bindValues(boolean putObjFieldsToMap, List<T> sourceObjects){
		    return sourceObjects.stream().map(obj->bindMappings(putObjFieldsToMap, new MappableMap(), obj))
										    .collect(Collectors.toList());
		}
		
		public MappableMap bindValue(T sourceObject){
			return bindMappings(true, new MappableMap(), sourceObject);
		}
		private MappableMap bindMappings(boolean putObjFieldsToMap, MappableMap mappingObject, T sourceObject){
			if(putObjFieldsToMap){
				mappingObject.from(sourceObject);
			}
		    mappingInfos.stream().forEach(maping->{
		    	if(maping.isMappingValueFunc()){
//			    	mappingObject.put(maping.jsonFieldName, maping.addMappingValueFunc.mapping(sourceObject, maping));
			    	mappingObject.put(maping.jsonFieldName, maping.addMappingValueFunc.mapping(sourceObject));
		    	}else{
			    	Object value = ReflectUtils.getProperty(sourceObject, maping.objectFieldName);
			    	mappingObject.put(maping.jsonFieldName, value);
		    	}
		    });
		    return mappingObject;
		}
		
	}

	public static interface MappingValueFunc<T, R> {
//		R mapping(T sourceObject, MappingInfo<T> mapping);
		R mapping(T sourceObject);
	}
	
	class MappingBuilder<T> {
		final private Object sourceObject;
		final private List<MappingInfo<T>> mappingInfos = new ArrayList<>();
		final private MappableMap mappingObject;
		public MappingBuilder(MappableMap mappingObject, Object sourceObject) {
	        super();
	        this.sourceObject = sourceObject;
	        this.mappingObject = mappingObject;
        }
		
		public MappingBuilder<T> mapping(String sourceName, String targetName){
			this.mappingInfos.add(new MappingInfo<T>(sourceName, targetName));
			return this;
		}

		
		public MappableMap bindValues(){
		    mappingInfos.stream().forEach(maping->{
		    	Object value = ReflectUtils.getProperty(sourceObject, maping.jsonFieldName);
		    	mappingObject.put(maping.objectFieldName, value);
		    });
		    return mappingObject;
		}
		
	}
	
	public static final class MappingInfo<T> {
		final private String jsonFieldName;
		final private String objectFieldName;
		final private MappingValueFunc<T, ?> addMappingValueFunc;
		public MappingInfo(String jsonFieldName, String objectFieldName) {
	        super();
	        this.jsonFieldName = jsonFieldName;
	        this.objectFieldName = objectFieldName;
	        this.addMappingValueFunc = null;
        }
		public MappingInfo(String sourceName, MappingValueFunc<T, ?> addMappingValueFunc) {
	        super();
	        this.jsonFieldName = sourceName;
	        this.objectFieldName = null;
	        this.addMappingValueFunc = addMappingValueFunc;
        }
		
		public boolean isMappingValueFunc(){
			return this.addMappingValueFunc!=null;
		}
		public String getJsonFieldName() {
			return jsonFieldName;
		}
		public String getObjectFieldName() {
			return objectFieldName;
		}
		public MappingValueFunc<T, ?> getAddMappingValueFunc() {
			return addMappingValueFunc;
		}
		
	}
	

}
