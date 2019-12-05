package org.onetwo.common.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.onetwo.common.ds.ContextHolder;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/****
 * 可通过这个类手动获取spring容器里注册的bean
 * @author weishao
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Springs {

	protected static Logger logger = JFishLoggerFactory.getLogger(Springs.class);

	private static Springs instance = new Springs();

	private ApplicationContext appContext;
	
	private boolean initialized;
	
//	private BaseEntityManager baseEntityManager;
	
	private ValidatorWrapper validatorWrapper;
	
	private ContextHolder contextHolder;
	
	private Springs() {
	}
	
	public static <T> T using(Class<T> clazz){
		return getInstance().getBean(clazz);
	}

	public static Springs getInstance() {
		return instance;
	}


	synchronized public static void initApplicationIfNotInitialized(ApplicationContext webappContext) {
		if(instance.initialized){
			return ;
		}
		if(webappContext!=null){
			initApplication(webappContext);
		}
	}
	public static void initApplication(ApplicationContext applicationContext) {
		Assert.notNull(applicationContext, "applicationContext can not be null");
		instance.appContext = applicationContext;
		instance.initialized = true;
		instance.printBeanNames();
		if(ConfigurableApplicationContext.class.isInstance(applicationContext)){
			((ConfigurableApplicationContext)applicationContext).registerShutdownHook();
		}
		applicationContext.publishEvent(new SpringsInitEvent(applicationContext));
	}
	
	public ApplicationContext getAppContext() {
		checkInitialized();
		return appContext;
	}
	
	public boolean isActive(){
		if(appContext instanceof ConfigurableApplicationContext){
			ConfigurableApplicationContext cac = (ConfigurableApplicationContext) appContext;
			return cac.isActive();
		}
		return isInitialized();
	}
	


	public void checkInitialized() {
		if(!isInitialized()){
			throw new BaseException("application context has not init ...");
		}
	}
	
	public boolean isInitialized() {
		return initialized;
	}

	public <T> void autoInject(T bean) {
		autoInject(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
	}

	public <T> void autoInject(T bean, int autowireMode) {
		checkInitialized();
		SpringUtils.injectAndInitialize(appContext.getAutowireCapableBeanFactory(), bean, autowireMode);
	}

	public ConfigurableApplicationContext getConfigurableAppContext() {
		return ConfigurableApplicationContext.class.isInstance(appContext)?(ConfigurableApplicationContext)appContext:null;
	}
	
	public Object getBean(String beanName) {
		return getBean(beanName, true);
	}

	public Object getBean(String beanName, boolean throwIfError) {
		checkInitialized();
		if(!getAppContext().containsBean(beanName)){
			if(throwIfError)
				throw new BaseException("not bean["+beanName+"] found! ");
			else
				logger.error("no bean["+beanName+"] found! ");
			return null;
		}
		Object bean = null;
		try {
			bean = getAppContext().getBean(beanName);
		} catch (Exception e) {
			if(throwIfError){
				throw new BaseException("get bean["+beanName+"] from spring error! ", e);
			}else{
				logger.info("get bean["+beanName+"] from spring error! ");
			}
		}
		return bean;
	}

	public <T> T getBean(Class<T> clazz, String beanName) {
		T bean = (T) getBean(beanName);
		if (bean == null)
			bean = getBean(clazz);
		return bean;
	}


	public <T> T getBeanByDefaultName(Class<T> clazz) {
		return (T)getBean(StringUtils.uncapitalize(clazz.getSimpleName()));
	}


	public Object getBeanByClassName(String className) {
		if(ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader())){
			return getBean(ReflectUtils.loadClass(className), false);
		}
		return null;
	}

	public boolean containsClassBean(String className) {
		return getBeanByClassName(className)!=null;
	}


	public <T> T getBean(Class<T> clazz) {
		return getBean(clazz, false);
	}

	public <T> T getBean(Class<T> clazz, boolean throwIfError) {
		checkInitialized();
		try {
			return instance.appContext.getBean(clazz);
		} catch (IllegalStateException | BeansException e) {
			if(throwIfError){
				throw e;
			}
			return null;
		}
	}
	
	public synchronized <T> T getOrRegisteredBean(Class<T> beanClass){
		T bean = getBean(beanClass);
		if(bean!=null)
			return bean;
		
		bean = SpringUtils.registerBean(getAppContext(), beanClass);
		//inject?
		return bean;
	}

	public <T> Map<String, T> getBeansMap(Class<T> clazz) {
		Map<String, T> map = BeanFactoryUtils.beansOfTypeIncludingAncestors(getAppContext(), clazz);
		return map;
	}
	
	public <T> List<T> getBeans(Class<T> clazz) {
//		Map map = getAppContext().getBeansOfType(clazz);
		Map map = SpringUtils.getBeansAsMap(getAppContext(), clazz);
		if(map==null || map.isEmpty())
			return Collections.EMPTY_LIST;
		List<T> list = new ArrayList<T>(map.values());
//		OrderComparator.sort(list);
		AnnotationAwareOrderComparator.sort(list);
		return list;
	}
	
	public <T> T getSpringHighestOrder(Class<T> clazz){
		return SpringUtils.getHighestOrder(getAppContext(), clazz);
	}
	
	public <T> T getSpringLowestOrder(Class<T> clazz){
		return SpringUtils.getLowestOrder(getAppContext(), clazz);
	}
	
	
	public <T> T getLastedImplementor(Class<T> clazz, boolean throwIfNo){
		T[] a = orderAscImplementor(clazz, throwIfNo);
		return a==null?null:a[a.length-1];
	}
	
	public <T> T getFirstImplementor(Class<T> clazz, boolean throwIfNo){
		T[] a = orderAscImplementor(clazz, throwIfNo);
		return a==null?null:a[0];
	}
	
	public <T> T[] orderAscImplementor(Class<T> clazz, boolean throwIfNo){
		Map<String, T> beans = getBeansMap(clazz);
		if(LangUtils.hasNotElement(beans)){
			if(throwIfNo)
				LangUtils.throwBaseException("can not find any bean for class : " + clazz);
			else
				return null;
		}
		T[] a = (T[])beans.values().toArray();
		if(a.length==1)
			return a;
		LangUtils.asc(a);
		return a;
	}
	
	public void printBeanNames(){
		String[] beanNames = getAppContext().getBeanDefinitionNames();
		StringBuilder buf = new StringBuilder(100);
		buf.append("\n=================================== spring beans ===================================\n");
		int index = 0;
		for (String bn : beanNames) {
			buf.append("[").append(++index).append("]").append(bn).append("\n");
		}
		buf.append("=================================== spring beans ===================================\n");
		if(logger.isInfoEnabled()){
			logger.info(buf.toString());
		}
	}

	/*public BaseEntityManager getBaseEntityManager() {
		BaseEntityManager be = baseEntityManager;
		if(be==null){
			be = getBean(BaseEntityManager.class);
			baseEntityManager = be;
		}
		return be;
	}
	*/

	public ValidatorWrapper getValidator(){
		if(this.validatorWrapper!=null) {
			return validatorWrapper;
		}
		
		synchronized (appContext) {
			ValidatorWrapper validatorWrapper = getBean(ValidatorWrapper.class);
			 if(validatorWrapper==null) {
				 throw new BaseException("ValidatorWrapper not found!");
			 }
			 this.validatorWrapper = validatorWrapper;
		}
		
		return validatorWrapper;
	}

	public ContextHolder getContextHolder() {
		ContextHolder ch = contextHolder;
		if(ch==null){
			ch = getBean(ContextHolder.class);
			contextHolder = ch;
		}
		return ch;
	}
	
	public static class SpringsInitEvent extends ApplicationContextEvent {
		/**
		 * 
		 */
		private static final long serialVersionUID = -198889043881295697L;

		public SpringsInitEvent(ApplicationContext source) {
			super(source);
		}
	}
}
