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

	public static SimpleResultBuilder simpleResult(){
		return new SimpleResultBuilder().succeed();
	}
	
	public static ListResultBuilder listResult(){
		return new ListResultBuilder().succeed();
	}
	
	public static MapResultBuilder mapResult(){
		return new MapResultBuilder().succeed();
	}
	
	public static LazyResultBuilder lazyResult(){
		return new LazyResultBuilder().succeed();
	}

	
	public static class LazyResultBuilder extends AbstractResultBuilder<LazyResult, LazyResultBuilder> {
		private LazyValue data;
		
		public LazyResultBuilder() {
			super(LazyResultBuilder.class);
		}
		
		public LazyResultBuilder data(LazyValue data){
			this.data = data;
			return this;
		}
		
		public LazyResult buildResult(){
			return LazyResult.create(code, message, data);
		}

	}

	public static class SimpleResultBuilder extends AbstractResultBuilder<SimpleDataResult<?>, SimpleResultBuilder> {
		
		private Object data;
		
		public SimpleResultBuilder() {
			super(SimpleResultBuilder.class);
		}
		
		public SimpleResultBuilder data(Object data){
			this.data = data;
			return this;
		}
		
		public SimpleDataResult<?> buildResult(){
			return SimpleDataResult.create(code, message, data);
		}
	}

	public static class ListResultBuilder extends AbstractResultBuilder<ListResult<?>, ListResultBuilder> {
		private List<Object> data;
		
		public ListResultBuilder() {
			super(ListResultBuilder.class);
			this.data = Lists.newArrayList();
		}
		
		public ListResultBuilder addData(Object... elements){
			Stream.of(elements).forEach(e->data.add(e));
			return this;
		}
		
		public ListResult<?> buildResult(){
			return ListResult.create(code, message, data);
		}
	}
	
	public static class MapResultBuilder extends AbstractResultBuilder<MapResult, MapResultBuilder> {
		
		private Map<Object, Object> data;
		
		public MapResultBuilder() {
			super(MapResultBuilder.class);
			this.data = Maps.newHashMap();
		}
		
		public MapResultBuilder put(Object key, Object value){
			data.put(key, value);
			return this;
		}
		
		public MapResult buildResult(){
			return MapResult.create(code, message, data);
		}
	}

	
}
