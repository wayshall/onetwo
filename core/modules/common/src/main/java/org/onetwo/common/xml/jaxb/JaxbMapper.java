/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.onetwo.common.xml.jaxb;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;


/**
 * copy from springside 
 * 
 * 使用Jaxb2.0实现XML<->Java Object的Mapper.
 * 
 * 在创建时需要设定所有需要序列化的Root对象的Class.
 * 特别支持Root对象是Collection的情形.
 * 
 * @author calvin
 */
public class JaxbMapper {
	
	public static JaxbMapper createMapper(Class<?>... rootTypes){
		Class<?>[] newRootTypes = (Class<?>[])ArrayUtils.add(rootTypes, CollectionWrapper.class);
		return new JaxbMapper(newRootTypes);
	}
	
	public static JaxbMapper createMapper(List<Class<?>> rootTypes){
		if(!rootTypes.contains(CollectionWrapper.class)){
			rootTypes.add(CollectionWrapper.class);
		}
		return new JaxbMapper(rootTypes.toArray(new Class<?>[rootTypes.size()]));
	}
	//多线程安全的Context.
	private JAXBContext jaxbContext;
	private Class<?>[] xmlRootTypes;

	/**
	 * @param rootTypes 所有需要序列化的Root对象的Class.
	 */
	public JaxbMapper(Class<?>... rootTypes) {
		try {
			jaxbContext = JAXBContext.newInstance(rootTypes);
			this.xmlRootTypes = rootTypes;
		} catch (JAXBException e) {
			LangUtils.throwBaseException(e);
		}
	}

	/**
	 * Java Object->Xml without encoding.
	 */
	public String toXml(Object root) {
		return toXml(root, null);
	}

	/**
	 * Java Object->Xml with encoding.
	 */
	public String toXml(Object root, String encoding) {
		try {
			StringWriter writer = new StringWriter();
			createMarshaller(encoding).marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e) {
			LangUtils.throwBaseException(e);
		}
		return null;
	}

	/**
	 * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形.
	 */
	public String toXml(Collection<?> root, String rootName) {
		return toXml(root, rootName, null);
	}

	/**
	 * Java Collection->Xml with encoding, 特别支持Root Element是Collection的情形.
	 */
	public String toXml(Collection<?> root, String rootName, String encoding) {
		try {
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;

			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
					CollectionWrapper.class, wrapper);

			StringWriter writer = new StringWriter();
			createMarshaller(encoding).marshal(wrapperElement, writer);

			return writer.toString();
		} catch (JAXBException e) {
			LangUtils.throwBaseException(e);
		}
		return null;
	}

	/**
	 * Xml->Java Object.
	 */
	public <T> T fromXml(String xml, Class<T> type) {
		try {
			StringReader reader = new StringReader(xml);
			JAXBElement<T> jaxb = createUnmarshaller().unmarshal(new StreamSource(reader), type);
			return jaxb.getValue();
		} catch (JAXBException e) {
			LangUtils.throwBaseException(e);
		}
		return null;
	}
	

	public <T> T fromFile(String path, Class<T> type) {
		try {
			InputStream in = FileUtils.getResourceAsStream(path);
			Reader reader = new InputStreamReader(in);
			JAXBElement<T> jaxb = createUnmarshaller().unmarshal(new StreamSource(reader), type);
			return jaxb.getValue();
		} catch (Exception e) {
			LangUtils.throwBaseException(e);
		}
		return null;
	}

	/**
	 * 创建Marshaller并设定encoding(可为null).
	 * 线程不安全，需要每次创建或pooling。
	 */
	public Marshaller createMarshaller(String encoding) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
//			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			
			if (StringUtils.isNotBlank(encoding)) {
				marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			}

			return marshaller;
		} catch (JAXBException e) {
			LangUtils.throwBaseException(e);
		}
		return null;
	}

	/**
	 * 创建UnMarshaller.
	 * 线程不安全，需要每次创建或pooling。
	 */
	public Unmarshaller createUnmarshaller() {
		try {
			Unmarshaller unm = jaxbContext.createUnmarshaller();
			return unm;
		} catch (JAXBException e) {
			LangUtils.throwBaseException(e);
		}
		return null;
	}
	
	public boolean isJaxbType(Class<?> clazz){
		return ArrayUtils.contains(xmlRootTypes, clazz);
	}

	/**
	 * 封装Root Element 是 Collection的情况.
	 */
	public static class CollectionWrapper<T> {

		@XmlAnyElement
		private Collection<T> collection = new ArrayList<T>();

		public void add(T e){
			collection.add(e);
		}

		@XmlTransient
		public Collection<T> getCollection() {
			return collection;
		}

		public void setCollection(Collection<T> collection) {
			this.collection = collection;
		}

		@SuppressWarnings("unchecked")
		public T getFirst(){
			return (T)LangUtils.getFirst(collection);
		}
		
	}
}
