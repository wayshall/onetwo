package org.onetwo.common.spring.web.mvc.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.onetwo.common.result.AbstractDataResult.SimpleDataResult;
import org.onetwo.common.result.LazyValue;
import org.onetwo.common.spring.validator.ValidatorUtils;
import org.onetwo.common.utils.CUtils;
import org.springframework.validation.BindingResult;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

final public class WebResultCreator {
	
	private final static WebResultCreator CREATOR = new WebResultCreator();

	private WebResultCreator() {
	}

	public static WebResultCreator creator(){
		return CREATOR;
	}

	public SimpleResultBuilder error(String message){
		SimpleResultBuilder builder = new SimpleResultBuilder().error(message);
		return builder;
	}

	public SimpleResultBuilder error(BindingResult bindingResult){
		String message = ValidatorUtils.asString(bindingResult);
		return error(message);
	}

	public SimpleResultBuilder success(String message){
		SimpleResultBuilder builder = new SimpleResultBuilder().success(message);
		return builder;
	}
	/***
	 * SimpleResultBuilder
	 * 把数据包装为{@linkplain org.onetwo.common.result.Result Result} ({@linkplain SimpleDataResult SimpleDataResult}) 类型返回
	 * 
	 * @return
	 */
	public SimpleResultBuilder simple(){
		return simple(null);
	}
	public SimpleResultBuilder simple(Object data){
		SimpleResultBuilder builder = new SimpleResultBuilder().success();
		builder.data(data);
		return builder;
	}
	
	/***
	 * 把data付给result的data属性，并设置extractableData属性为true
	 * @param data
	 * @return
	 */
	public SimpleResultBuilder extractableData(Object data){
		return simple(data).extractableData(true);
	}
	/****
	 * 
	 * ListResultBuilder
	 * 把数据包装为{@linkplain org.onetwo.common.result.Result Result} ({@linkplain org.onetwo.common.result.AbstractDataResult.ListResult ListResult}) 类型返回
	 * 
	 * @return
	 */
	public ListResultBuilder list(Object...elements){
		ListResultBuilder builder = new ListResultBuilder().success();
		builder.addElements(elements);
		return builder;
	}
	public ListResultBuilder list(List<?> list){
		ListResultBuilder builder = new ListResultBuilder().success();
		builder.addList(list);
		return builder;
	}
	public ListResultBuilder extractedList(List<Object> list){
		return list(list).extractableData(true);
	}
	/***
	 * 
	 * MapResultBuilder
	 * 把数据包装为{@linkplain org.onetwo.common.result.Result Result} ({@linkplain org.onetwo.common.result.MapResult MapResult}) 类型返回
	 * 
	 * @return
	 */
	public MapResultBuilder map(Object...objects){
		MapResultBuilder builder = new MapResultBuilder().success();
		builder.puts(objects);
		return builder;
	}
	public MapResultBuilder extractedMap(Object...objects){
		return map(objects).extractableData(true);
	}
	
	public LazyResultBuilder lazy(LazyValue data){
		LazyResultBuilder builder = new LazyResultBuilder().success();
		builder.data(data);
		return builder;
	}



	public static class SimpleResultBuilder extends AbstractResultBuilder<Object, SimpleResultBuilder> {
	}
	public static class LazyResultBuilder extends AbstractResultBuilder<LazyValue, LazyResultBuilder> {
	}
	

	public static class ListResultBuilder extends AbstractResultBuilder<List<Object>, ListResultBuilder> {
		
		public ListResultBuilder() {
//			super(ListResultBuilder.class);
			this.data = Lists.newArrayList();
		}
		
		public ListResultBuilder addElements(Object... elements){
			Stream.of(elements).forEach(e->data.add(e));
			return this;
		}
		
		public ListResultBuilder addList(List<?> list){
			data.addAll(list);
			return this;
		}
	}
	
	public static class MapResultBuilder extends AbstractResultBuilder<Map<Object, Object>, MapResultBuilder> {
		
		public MapResultBuilder() {
//			super(MapResultBuilder.class);
			this.data = Maps.newHashMap();
		}
		
		public MapResultBuilder put(Object key, Object value){
			data.put(key, value);
			return this;
		}
		
		public MapResultBuilder puts(Object...objs){
			if(objs.length==0)
				return this;
			data.putAll(CUtils.asMap(objs));
			return this;
		}
	}

	
}
