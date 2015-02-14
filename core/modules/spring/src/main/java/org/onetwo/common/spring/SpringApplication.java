package org.onetwo.common.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.OrderComparator;

/****
 * 可通过这个类手动获取spring容器里注册的bean
 * @author weishao
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SpringApplication {

	protected static Logger logger = Logger.getLogger(SpringApplication.class);

	private static SpringApplication instance = new SpringApplication();

	private ApplicationContext appContext;
	
	private boolean initialized;
	
	private BaseEntityManager baseEntityManager;
	
	private ValidatorWrapper validatorWrapper;
	
	private ContextHolder contextHolder;
	
	private SpringApplication() {
	}

	public static SpringApplication getInstance() {
		if(!instance.initialized){
			throw new BaseException("application context has not init ...");
		}
		return instance;
	}


	public static void initApplicationIfNotInitialized(ApplicationContext webappContext) {
		if(instance.initialized){
			return ;
		}
		initApplication(webappContext);
	}
	public static void initApplication(ApplicationContext webappContext) {
		instance.appContext = webappContext;
		instance.initialized = true;
//		instance.printBeanNames();
		try {
			if(ConfigurableApplicationContext.class.isInstance(webappContext))
				((ConfigurableApplicationContext)webappContext).registerShutdownHook();
		} catch (Exception e) {
			logger.error("can not find the BaseEntityManager, ignore it: " + e.getMessage());
		}
	}

	public ApplicationContext getAppContext() {
		return appContext;
	}
	

	public <T> void autoInject(T bean) {
		autoInject(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
	}

	public <T> void autoInject(T bean, int autowireMode) {
		SpringUtils.injectAndInitialize(appContext.getAutowireCapableBeanFactory(), bean, autowireMode);
	}

	public ConfigurableApplicationContext getConfigurableAppContext() {
		return ConfigurableApplicationContext.class.isInstance(appContext)?(ConfigurableApplicationContext)appContext:null;
	}
	
	public Object getBean(String beanName) {
		return getBean(beanName, false);
	}

	public Object getBean(String beanName, boolean throwIfError) {
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
//			logger.error("get bean["+beanName+"] from spring error! ");
			if(throwIfError)
				throw new BaseException("get bean["+beanName+"] from spring error! ", e);
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


	public <T> T getBean(Class<T> clazz) {
		return getBean(clazz, false);
	}

	public <T> T getBean(Class<T> clazz, boolean throwIfError) {
		T bean = null;
		String beanName = StringUtils.uncapitalize(clazz.getSimpleName());
//			Map map = this.getAppContext().getBeansOfType(clazz);
		Map map = getBeansMap(clazz);
		if (map == null || map.isEmpty())
			return (T) getBean(beanName, throwIfError);
		else
			bean = (T) map.get(beanName);
		if (bean == null)
			bean = (T) map.values().iterator().next();
		
		return bean;
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
		OrderComparator.sort(list);
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
		System.out.println("=================================== spring beans ===================================");
		int index = 0;
		for (String bn : beanNames) {
			Object obj = SpringApplication.getInstance().getBean(bn);
			System.out.println("["+(++index)+"]" + bn + ":" + (obj != null ? obj.getClass() : "null"));
		}
		System.out.println("=================================== spring beans ===================================");
	}

	public BaseEntityManager getBaseEntityManager() {
		BaseEntityManager be = baseEntityManager;
		if(be==null){
			be = getBean(BaseEntityManager.class);
			baseEntityManager = be;
		}
		return be;
	}
	

	public ValidatorWrapper getValidator(){
		if(this.validatorWrapper!=null)
			return validatorWrapper;
		
		this.validatorWrapper = getBean(ValidatorWrapper.class);
		 if(validatorWrapper==null)
			 throw new BaseException("no ValidatorWrapper found!");
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

}
