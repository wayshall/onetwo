package org.onetwo.boot.core.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.onetwo.common.result.AbstractDataResult.LazyResult;
import org.onetwo.common.result.AbstractDataResult.ListResult;
import org.onetwo.common.result.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.result.LazyValue;
import org.onetwo.common.result.MapResult;
import org.onetwo.common.utils.CUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings("unchecked")
final public class WebResultCreator {

	private WebResultCreator() {
	}

	public static WebResultCreator result(){
		return new WebResultCreator();
	}
	/***
	 * SimpleResultBuilder
	 * 把数据包装为{@linkplain org.onetwo.common.result.Result Result} ({@linkplain SimpleDataResult SimpleDataResult}) 类型返回
	 * 
	 * @return
	 */
	public <T> SimpleResultBuilder<T> simple(){
		return simple(null);
	}
	public <T> SimpleResultBuilder<T> simple(T data){
		SimpleResultBuilder<T> builder = new SimpleResultBuilder<T>().success();
		builder.data(data);
		return builder;
	}
	/****
	 * 
	 * ListResultBuilder
	 * 把数据包装为{@linkplain org.onetwo.common.result.Result Result} ({@linkplain org.onetwo.common.result.AbstractDataResult.ListResult ListResult}) 类型返回
	 * 
	 * @return
	 */
	public <T> ListResultBuilder<T> list(){
		return new ListResultBuilder<T>().success();
	}
	public <T> ListResultBuilder<T> list(T...elements){
		ListResultBuilder<T> builder = new ListResultBuilder<T>().success();
		builder.addElements(elements);
		return builder;
	}
	public <T> ListResultBuilder<T> list(List<T> list){
		ListResultBuilder<T> builder = new ListResultBuilder<T>().success();
		builder.addList(list);
		return builder;
	}
	/***
	 * 
	 * MapResultBuilder
	 * 把数据包装为{@linkplain org.onetwo.common.result.Result Result} ({@linkplain org.onetwo.common.result.MapResult MapResult}) 类型返回
	 * 
	 * @return
	 */
	public MapResultBuilder map(){
		return new MapResultBuilder().success();
	}
	public MapResultBuilder map(Object...objects){
		MapResultBuilder builder = new MapResultBuilder().success();
		builder.puts(objects);
		return builder;
	}
	
	public LazyResultBuilder lazy(){
		return new LazyResultBuilder().success();
	}
	
	public LazyResultBuilder lazy(LazyValue data){
		LazyResultBuilder builder = new LazyResultBuilder().success();
		builder.data(data);
		return builder;
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
		
		public ListResultBuilder<T> addElements(T... elements){
			Stream.of(elements).forEach(e->data.add(e));
			return this;
		}
		
		public ListResultBuilder<T> addList(List<T> list){
			data.addAll(list);
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
		
		public MapResultBuilder puts(Object...objs){
			data.putAll(CUtils.asMap(objs));
			return this;
		}
		
		@Override
		protected MapResult creeateResult(){
			return MapResult.create(code, message, data);
		}
	}

	
}
