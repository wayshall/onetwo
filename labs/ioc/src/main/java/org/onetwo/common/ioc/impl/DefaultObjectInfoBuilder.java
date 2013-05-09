package org.onetwo.common.ioc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.ioc.InnerContainer;
import org.onetwo.common.ioc.ObjectInfo;
import org.onetwo.common.ioc.ObjectInfoBuilder;
import org.onetwo.common.ioc.ObjectInitialer;
import org.onetwo.common.ioc.Valuer;
import org.onetwo.common.ioc.inject.AnnotationFacotry;
import org.onetwo.common.ioc.inject.ComponentMetaTransfor;
import org.onetwo.common.ioc.proxy.BFInterceptor;
import org.onetwo.common.ioc.proxy.BFProxyHandler;
import org.onetwo.common.ioc.proxy.DynamicProxyFactory;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.ObjectUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "unchecked", "serial" })
public class DefaultObjectInfoBuilder implements ObjectInfoBuilder {
	
	public static final String DEFAULT_INTERCEPTORS_NAME = "_DEFAULT_INTERCEPTORS";
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private InnerContainer innerContainer;
	
	private AnnotationFacotry metaFacotry = AnnotationFacotry.getInstance();
	
	private ObjectInitialer objectInitialer;
	
	public DefaultObjectInfoBuilder(InnerContainer container, ObjectInitialer objectInitialer) {
		this.innerContainer = container;
		this.objectInitialer = objectInitialer;
	}

	public void setInnerContainer(InnerContainer innerContainer) {
		this.innerContainer = innerContainer;
	}

	public InnerContainer getInnerContainer() {
		return innerContainer;
	}

	public ObjectInfo build(String name, Object val){
		Object t = this.objectInitialer.initializeObject(val);
		if(t==null)
			throw new ServiceException("no object. name : " + val);
		return createObjectInfo(name, t);
	}
	
	public ObjectInfo buildByList(String name, Object...args){
		Assert.notEmpty(args);
		List objs = new ArrayList();
		Object obj = null;
		for(Object o : args){
			obj = this.objectInitialer.initializeObject(o);
			objs.add(obj);
		}
		return createObjectInfo(name, objs);
	}
	
	public ObjectInfo buildByList(String name, List args){
		Assert.notEmpty(args);
		List beanList = new ArrayList();
		for(Object o : args){
			Object obj = this.objectInitialer.initializeObject(o);
			beanList.add(obj);
		}
		return createObjectInfo(name, beanList);
	}

	
	public ObjectInfo buildByMap(String name, Object...args){
		return buildByMap(name, MyUtils.convertParamMap(args));
	}
	
	public ObjectInfo buildByMap(String name, Map map){
		Assert.notEmpty(map);
		Map beanMap = new HashMap();
		for(Map.Entry entry : (Set<Map.Entry>)map.entrySet()){
			Object obj = this.objectInitialer.initializeObject(entry.getValue());
			beanMap.put(entry.getKey(), obj);
		}
		return createObjectInfo(name, beanMap);
	}
	
	protected ObjectInfo createObjectInfo(String name, Object bean){
		Valuer valuer = Valuer.val(bean);
		ComponentMetaTransfor transfor = this.metaFacotry.createComponentMetaTransfor(valuer.getValue());
		name = transfor.getBeanName(name);
		
		if(innerContainer.contains(name))
			throw new ServiceException("object name has esixt. name:" + name+", class:"+bean.getClass());

		ObjectInfo objInfo = null;
		BFProxyHandler handler = null;
		if(transfor.needProxy()){
			handler = buildProxyHandler(transfor);
		}
		if(handler!=null){
			valuer.setValue(handler);
			objInfo = new ProxyObjectInfo(name, valuer);
		}
		else
			objInfo = new DefaultObjectInfo(name, valuer);
		return objInfo;
	}
	
	/*protected Class[] getInterceptorClasses(Object bean){
		Component comp = bean.getClass().getAnnotation(Component.class);
		if(comp==null)
			return null;
		Class[] inteceptors = comp.interceptors();
		if(ArrayUtils.contains(inteceptors, Object.class))
			inteceptors = (Class[])ArrayUtils.removeElement(inteceptors, Object.class);
		BFInterceptors bfinterceptors = bean.getClass().getAnnotation(BFInterceptors.class);
		if(bfinterceptors!=null){
			Class[] bfCls = bfinterceptors.value();
			if(bfCls!=null && bfCls.length>0){
				inteceptors = (Class[])ArrayUtils.addAll(inteceptors, bfCls);
			}
		}
		return inteceptors;
	}
	
	protected Class[] getBusinessInterfaces(Object bean){
		Component comp = bean.getClass().getAnnotation(Component.class);
		if(comp==null)
			return null;
		Class[] interfaces = comp.businessInterfaces();
		if(ArrayUtils.contains(interfaces, Object.class))
			interfaces = (Class[])ArrayUtils.removeElement(interfaces, Object.class);
		return interfaces;
	}*/
	
	protected BFProxyHandler buildProxyHandler(ComponentMetaTransfor transfor){
		Class[] bfInterceptorClasses = transfor.getInterceptorClasses();
		Class[] businessInterfaces = transfor.getBusinessInterfaces();
		
		if(businessInterfaces==null || businessInterfaces.length==0){
			if(!LangUtils.isEmpty(bfInterceptorClasses)){
				//no businessInterface but config the interceptorClass
				String msg = "the bean["+transfor.getBean().getClass()+"]'s  businessInterfaces is empty, can not support interceptor : " + StringUtils.buildString(bfInterceptorClasses);
				logger.warn(msg);
				throw new ServiceException(msg);
			}
			//no businessInterface,igonre...
			return null;
		}
		
		List<BFInterceptor> bfi = registerInterceptor(bfInterceptorClasses);
		if(bfi==null || bfi.isEmpty())
			return null;
		BFProxyHandler proxyHandler = DynamicProxyFactory.proxy(transfor.getBean(), businessInterfaces, bfi);
		return proxyHandler;
	}
	
	protected List<BFInterceptor> registerInterceptor(Class... bfInterceptorClasses){
		List<BFInterceptor> bfs = new ArrayList<BFInterceptor>();
		List<BFInterceptor> defInterceptors = getDefaultInterceptors();
		if(defInterceptors!=null && !defInterceptors.isEmpty()){
			bfs.addAll(defInterceptors);
		}
		if(bfInterceptorClasses==null || bfInterceptorClasses.length==0)
			return bfs;
		ObjectInfo info = null;
		String bname = null;
		for(Class cls : bfInterceptorClasses){
			bname = cls.getName();
			if(innerContainer.contains(bname)){
				info = innerContainer.getObjectInfo(bname, true);
			}
			else{
				info = innerContainer.registerClass(bname, cls);
			}
			bfs.add((BFInterceptor)info.getBean());
		}
		return bfs;
	}

	
	protected List<BFInterceptor> getDefaultInterceptors(){
		if(!innerContainer.contains(DEFAULT_INTERCEPTORS_NAME))
			return null;
		List<BFInterceptor> defs = innerContainer.getObject(DEFAULT_INTERCEPTORS_NAME);
		return defs;
	}
	
	public ObjectInfo buildByClass(String name, Class clazz, Object...objects){
		Object obj = null;
		if(!ObjectUtils.isEmpty(objects)){
			obj = ReflectUtils.newInstance(clazz, objects);
		}else{
			obj = ReflectUtils.newInstance(clazz);
		}
		return createObjectInfo(name, obj);
	}
}
