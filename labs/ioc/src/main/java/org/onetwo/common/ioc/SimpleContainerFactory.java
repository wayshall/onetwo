package org.onetwo.common.ioc;

import org.onetwo.common.ioc.impl.DefaultObjectFactory;


@SuppressWarnings("unchecked")
public class SimpleContainerFactory {
	
	private SimpleContainerFactory(){
	}
	
	public static Container create(){
		DefaultObjectFactory container = new DefaultObjectFactory();
		return container;
	}
	
	public static Container create(Class...filterClasses){
		DefaultObjectFactory container = new DefaultObjectFactory();
		container.setInfoBuilder(BFComponentFacotry.createDefaultObjectInfoBuilder(container));
		container.setBinder(BFComponentFacotry.createFilterObjectBinder(container, filterClasses));
		container.setInjectProcessor(BFComponentFacotry.createDefaultInjectProcessor(container));
		return container;
	}
	
}
