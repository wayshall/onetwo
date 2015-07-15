package org.onetwo.easyui;

import java.util.List;

import org.onetwo.common.utils.map.MappableMap;
import org.onetwo.common.utils.map.MappableMap.MappingValueFunc;
import org.onetwo.common.utils.map.MappableMap.StaticMappingBuilder;

@SuppressWarnings("unchecked")
public class EasyBuilder<B, E> {
	protected StaticMappingBuilder<E> builder = MappableMap.newMappingBuilder();

	public B mapAllFields(){
		this.builder.mapAllFields();
		return (B)this;
	}
	public B specifyMappedFields(){
		this.builder.specifyMappedFields();
		return (B)this;
	}
	
	public B addMapping(String fieldName, MappingValueFunc<E, ?> valueFunc){
		builder.addMapping(fieldName, valueFunc);
		return (B)this;
	}
	
	public B addMapping(String fieldName, String objectFieldName){
		builder.addMapping(fieldName, objectFieldName);
		return (B)this;
	}

	public List<MappableMap> build(List<E> sourceObjects){
		List<MappableMap> treeDatas = builder.bindValues(sourceObjects);
		return treeDatas;
	}

	public MappableMap build(E sourceObjects){
		MappableMap mappableMap = builder.bindValue(sourceObjects);
		return mappableMap;
	}
	
	public static class SimpleEasyBuilder<T> extends EasyBuilder<SimpleEasyBuilder<T>, T> {
	}
}
