package org.onetwo.common.ioc;

import java.util.Arrays;

import org.onetwo.common.ioc.impl.DefaultInjectProcessor;
import org.onetwo.common.ioc.impl.DefaultObjectBinder;
import org.onetwo.common.ioc.impl.DefaultObjectInfoBuilder;
import org.onetwo.common.ioc.impl.DefaultObjectInitialer;
import org.onetwo.common.ioc.impl.FilterObjectBinder;


@SuppressWarnings("unchecked")
abstract public class BFComponentFacotry {
	
	public static ObjectInfoBuilder createDefaultObjectInfoBuilder(InnerContainer container){
		ObjectInitialer objectInitialer = createObjectInitialer(container);
		return new DefaultObjectInfoBuilder(container, objectInitialer);
	}
	
	public static ObjectBinder createDefaultObjectBinder(Container container){
		return new DefaultObjectBinder(container);
	}
	
	public static InjectProcessor createDefaultInjectProcessor(InnerContainer container){
		return new DefaultInjectProcessor(container);
	}
	
	public static ObjectBinder createFilterObjectBinder(Container container, Class...filterClasses){
		FilterObjectBinder binder = new FilterObjectBinder(container);
		binder.setIgnoreClasses(Arrays.asList(filterClasses));
		return binder;
	}
	
	public static ObjectInitialer createObjectInitialer(Container container){
		return new DefaultObjectInitialer(container);
	}
	
}
