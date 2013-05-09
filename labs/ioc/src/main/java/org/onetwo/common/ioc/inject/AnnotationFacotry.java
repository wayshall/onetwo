package org.onetwo.common.ioc.inject;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.ioc.inject.impl.InjectLocalParser;
import org.onetwo.common.ioc.inject.impl.InjectParser;
import org.onetwo.common.ioc.inject.impl.InjectRemoteParser;

public class AnnotationFacotry {
	
	private static AnnotationFacotry instance = new AnnotationFacotry();
	
	public static AnnotationFacotry getInstance(){
		return instance;
	}

	private List<InjectAnnotationParser> injectAnnotaionParsers = new ArrayList<InjectAnnotationParser>(3);
	
	private AnnotationFacotry(){
		injectAnnotaionParsers.add(new InjectParser());
		injectAnnotaionParsers.add(new InjectLocalParser());
		injectAnnotaionParsers.add(new InjectRemoteParser());
	}
	
	public ComponentMetaTransfor createComponentMetaTransfor(Object bean){
		return new DefaultComponentMetaTransfor(bean);
	}

	public List<InjectAnnotationParser> getInjectAnnotaionParsers() {
		return injectAnnotaionParsers;
	}
	
}
