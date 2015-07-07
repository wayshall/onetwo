package org.onetwo.easyui;

import java.util.List;

import org.onetwo.common.utils.map.MappableMap;
import org.onetwo.common.utils.map.MappableMap.MappingValueFunc;
import org.onetwo.common.utils.map.MappableMap.StaticMappingBuilder;

@SuppressWarnings("unchecked")
public class EasyBuilder<S, D> {
	protected StaticMappingBuilder<D> builder = MappableMap.newMappingBuilder();
	
	public S addMapping(String fieldName, MappingValueFunc<D> valueFunc){
		builder.addMapping(fieldName, valueFunc);
		return (S)this;
	}
	
	public S addMapping(String fieldName, String objectFieldName){
		builder.addMapping(fieldName, objectFieldName);
		return (S)this;
	}

	public List<MappableMap> build(List<D> sourceObjects){
		List<MappableMap> treeDatas = builder.bindValues(sourceObjects);
		return treeDatas;
	}
	
	public static class SimpleEasyBuilder<T> extends EasyBuilder<SimpleEasyBuilder<T>, T> {
	}
}
