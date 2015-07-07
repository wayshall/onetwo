package org.onetwo.easyui;

import org.onetwo.common.utils.map.MappableMap.MappingValueFunc;
import org.onetwo.easyui.EasyBuilder.SimpleEasyBuilder;

final public class EasyModel {
	public static <T> SimpleEasyBuilder<T> newSimpleBuilder(Class<T> clazz){
		return new SimpleEasyBuilder<T>();
	}
	public static <T> EasyTreeBuilder<T> newTreeBuilder(Class<T> clazz){
		return new EasyTreeBuilder<T>();
	}
	
	public static class EasyTreeBuilder<R> extends EasyBuilder<EasyTreeBuilder<R>, R>{
		
		public EasyTreeBuilder<R> id(String fieldName){
			addMapping("id", fieldName);
			return this;
		}
		
		public EasyTreeBuilder<R> state(String fieldName){
			addMapping("state", fieldName);
			return this;
		}
		
		public EasyTreeBuilder<R> state(MappingValueFunc<R> valueFunc){
			addMapping("state", valueFunc);
			return this;
		}

	}
	
	private EasyModel(){}

}
