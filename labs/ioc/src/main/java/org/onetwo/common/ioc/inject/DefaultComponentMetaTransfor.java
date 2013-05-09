package org.onetwo.common.ioc.inject;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.onetwo.common.ioc.ContainerAware;
import org.onetwo.common.ioc.Container.Destroyable;
import org.onetwo.common.ioc.Container.InitializingBean;
import org.onetwo.common.ioc.annotation.BFComponent;
import org.onetwo.common.ioc.annotation.BFInterceptors;
import org.onetwo.common.ioc.proxy.BFInterceptor;
import org.onetwo.common.utils.CollectionUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.Predicate;

@SuppressWarnings({ "unchecked", "serial" })
public class DefaultComponentMetaTransfor implements ComponentMetaTransfor {
	

	public static Collection<Class> excludeInterface = new HashSet<Class>(){
		{
			add(ContainerAware.class);
			add(InitializingBean.class);
			add(Destroyable.class);
			add(BFInterceptor.class);
		}
	};
	
	private Predicate filterInterfacePredicate = new Predicate(){

		@Override
		public boolean evaluate(Object object) {
			Class interfaceClass = (Class) object;
			if(excludeInterface.contains(object))
				return false;
			if(interfaceClass.getName().startsWith("java."))
				return false;
			return true;
		}
		
	};
	
	
	private Object bean;
	private BFComponent component;
	private Class[] interceptorClasses = null;
	private Class[] businessInterfaces = null;
	
	public DefaultComponentMetaTransfor(Object bean){
		this.bean = bean;
		this.component = bean.getClass().getAnnotation(BFComponent.class);
		/*if(component==null)
			throw new ServiceException("the bean["+bean+"] has not any component annotation.");*/
		this.interceptorClasses = parseInterceptorClasses();
		this.businessInterfaces = parseBusinessInterfaces();
	}

	protected Class[] parseInterceptorClasses(){
		Class[] inteceptors = null;
		
		if(component!=null){
			inteceptors = component.interceptors();
			if(ArrayUtils.contains(inteceptors, Object.class))
				inteceptors = (Class[])ArrayUtils.removeElement(inteceptors, Object.class);
		}
		
		BFInterceptors bfinterceptors = bean.getClass().getAnnotation(BFInterceptors.class);
		if(bfinterceptors!=null){
			Class[] bfCls = bfinterceptors.value();
			if(bfCls!=null && bfCls.length>0){
				inteceptors = (Class[])ArrayUtils.addAll(inteceptors, bfCls);
			}
		}
		
		return inteceptors;
	}
	

	protected Class[] parseBusinessInterfaces(){
		if(component==null)
			return null;
		Class[] interfaces = component.businessInterfaces();
		if(ArrayUtils.contains(interfaces, Object.class))
			interfaces = (Class[])ArrayUtils.removeElement(interfaces, Object.class);
		return interfaces;
	}
	
	public String getBeanName(String name){
		String newName = name;
		if(StringUtils.isBlank(newName))
			newName = bean.getClass().getSimpleName();
		if(component!=null && StringUtils.isNotBlank(component.name()))
			newName = component.name();
		return newName;
	}

	
	public Class[] getInterceptorClasses(){
		return interceptorClasses;
	}
	
	public Class[] getBusinessInterfaces() {
		return businessInterfaces;
	}

	protected Class[] getBusinessInterfaces2(Object bean){
		List<Class> interfaces = MyUtils.asList(bean.getClass().getInterfaces());
		CollectionUtils.filter(interfaces, filterInterfacePredicate);
		return interfaces.toArray(new Class[interfaces.size()]);
	}
	
	public Object getBean() {
		return bean;
	}
	
	public boolean needProxy(){
		return !ArrayUtils.isEmpty(this.businessInterfaces) && !ArrayUtils.isEmpty(this.interceptorClasses); 
	}

}