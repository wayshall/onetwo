package org.onetwo.common.jfishdbm.jdbc.type.impl;

import java.util.Map;
import java.util.Optional;

import org.onetwo.common.jfishdbm.jdbc.type.MappedTypeHandler;
import org.onetwo.common.jfishdbm.jdbc.type.TypeHandler;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

public class DefaultTypeHandlerManager {
	
	private Map<Class<?>, TypeHandler<?>> javaTypeHandlerMapper = Maps.newHashMap();
	private Map<Integer, TypeHandler<?>> sqlTypeHandlerMapper = Maps.newHashMap();
	private TypeHandler<Object> defaultTypeHandler = null;

	public void register(MappedTypeHandler<?> handler){
		Assert.notEmpty(handler.getMappedJavaTypes());
		handler.getMappedJavaTypes().forEach(type->register(type, handler));
		Assert.notEmpty(handler.getMappedJavaTypes());
		handler.getMappedJavaTypes().forEach(type->register(type, handler));
	}
	public void register(Class<?> javaType, TypeHandler<?> handler){
		javaTypeHandlerMapper.put(javaType, handler);
	}
	public void register(int sqlType, TypeHandler<?> handler){
		sqlTypeHandlerMapper.put(sqlType, handler);
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<TypeHandler<T>> getTypeHandler(Class<T> javaType){
		TypeHandler<T> handler = (TypeHandler<T>)javaTypeHandlerMapper.get(javaType);
		return Optional.ofNullable(handler);
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<TypeHandler<T>> getTypeHandler(int sqlType){
		TypeHandler<T> handler = (TypeHandler<T>)sqlTypeHandlerMapper.get(sqlType);
		return Optional.ofNullable(handler);
	}

	public TypeHandler<Object> getDefaultTypeHandler() {
		return defaultTypeHandler;
	}
	

}
