package org.onetwo.boot.core.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.onetwo.common.result.AbstractDataResult.LazyResult;
import org.onetwo.common.result.AbstractDataResult.ListResult;
import org.onetwo.common.result.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.result.LazyValue;
import org.onetwo.common.result.MapResult;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

final public class WebResultCreator {

	private WebResultCreator() {
	}

	public static <T> SimpleResultBuilder<T> simpleResult(){
		return new SimpleResultBuilder<T>().success();
	}
	
	public static <T> ListResultBuilder<T> listResult(){
		return new ListResultBuilder<T>().success();
	}
	
	public static MapResultBuilder mapResult(){
		return new MapResultBuilder().success();
	}
	
	public static LazyResultBuilder lazyResult(){
		return new LazyResultBuilder().success();
	}

	
	public static class LazyResultBuilder extends AbstractResultBuilder<LazyResult, LazyResultBuilder> {
		private LazyValue data;
		
		public LazyResultBuilder() {
//			super(LazyResultBuilder.class);
		}
		
		public LazyResultBuilder data(LazyValue data){
			this.data = data;
			return this;
		}
		
		@Override
		protected LazyResult creeateResult(){
			return LazyResult.create(code, message, data);
		}

	}

	public static class SimpleResultBuilder<T> extends AbstractResultBuilder<SimpleDataResult<?>, SimpleResultBuilder<T>> {
		
		private T data;
		
		public SimpleResultBuilder() {
//			super(SimpleResultBuilder.class);
		}
		
		public SimpleResultBuilder<T> data(T data){
			this.data = data;
			return this;
		}
		
		@Override
		protected SimpleDataResult<T> creeateResult(){
			return SimpleDataResult.create(code, message, data);
		}
	}

	public static class ListResultBuilder<T> extends AbstractResultBuilder<ListResult<T>, ListResultBuilder<T>> {
		private List<T> data;
		
		public ListResultBuilder() {
//			super(ListResultBuilder.class);
			this.data = Lists.newArrayList();
		}
		
		@SuppressWarnings("unchecked")
		public ListResultBuilder<T> addData(T... elements){
			Stream.of(elements).forEach(e->data.add(e));
			return this;
		}
		
		@Override
		protected ListResult<T> creeateResult(){
			return ListResult.create(code, message, data);
		}
	}
	
	public static class MapResultBuilder extends AbstractResultBuilder<MapResult, MapResultBuilder> {
		
		private Map<Object, Object> data;
		
		public MapResultBuilder() {
//			super(MapResultBuilder.class);
			this.data = Maps.newHashMap();
		}
		
		public MapResultBuilder put(Object key, Object value){
			data.put(key, value);
			return this;
		}
		
		@Override
		protected MapResult creeateResult(){
			return MapResult.create(code, message, data);
		}
	}

	
}
