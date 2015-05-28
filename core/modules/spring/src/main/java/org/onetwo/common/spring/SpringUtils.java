package org.onetwo.common.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.onetwo.common.spring.utils.BeanMapWrapper;
import org.onetwo.common.spring.utils.JFishPropertiesFactoryBean;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.common.utils.propconf.PropUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/****
 * 可通过这个类手动获取spring容器里注册的bean
 * @author weishao
 *
 */
@SuppressWarnings({"unchecked"})
final public class SpringUtils {

	protected static Logger logger = JFishLoggerFactory.getLogger(SpringUtils.class);
	private static String ACTIVE_PROFILES = "spring.profiles.active";
//	private static String CLASSPATH = "classpath:";
	
	private SpringUtils(){
	}
	
	public static void setProfiles(String profiles){
		System.setProperty(ACTIVE_PROFILES, profiles);
	}
	
	public static String getProfiles(){
		return System.getProperty(ACTIVE_PROFILES);
	}
	
	public static void addProfiles(String... profiles){
		if(LangUtils.isEmpty(profiles))
			return ;
		String existProfiles = getProfiles();
		String newProfiles = StringUtils.join(profiles, ",");
		if(StringUtils.isNotBlank(existProfiles)){
			newProfiles = existProfiles + newProfiles;
		}
		setProfiles(newProfiles);
	}

	public static <T> List<T> getBeans(ApplicationContext appContext, Class<T> clazz) {
		Map<String, T> beanMaps = BeanFactoryUtils.beansOfTypeIncludingAncestors(appContext, clazz);
		if(beanMaps==null || beanMaps.isEmpty())
			return Collections.EMPTY_LIST;
		List<T> list = new JFishList<T>(beanMaps.values());
		OrderComparator.sort(list);
		return list;
	}
	
	public static <T> Map<String, T> getBeansAsMap(ApplicationContext appContext, Class<T> clazz) {
		Map<String, T> beanMaps = BeanFactoryUtils.beansOfTypeIncludingAncestors(appContext, clazz);
		return beanMaps;
	}
	
	public static <T> T getBean(ApplicationContext appContext, Class<T> clazz) {
		List<T> beans = getBeans(appContext, clazz);
		return (T)LangUtils.getFirst(beans);
	}
	
	public static <T> T getBean(ApplicationContext appContext, String beanName) {
		return (T)appContext.getBean(beanName);
	}

	
	public static <T> T getHighestOrder(ApplicationContext appContext, Class<T> clazz){
		List<T> list = getBeans(appContext, clazz);
		if(LangUtils.isEmpty(list))
			return null;
		else
			return list.get(0);
	}
	
	public static <T> T getLowestOrder(ApplicationContext appContext, Class<T> clazz){
		List<T> list = getBeans(appContext, clazz);
		if(LangUtils.isEmpty(list))
			return null;
		else
			return list.get(list.size()-1);
	}
	

	public static PropertyPlaceholderConfigurer newApplicationConf(String... locations){
		return newApplicationConf(true, locations);
	}
	
	public static PropertyPlaceholderConfigurer newApplicationConf(boolean searchSystemEnvironment, String... locations){
		Assert.notEmpty(locations);
		PropertyPlaceholderConfigurer ppc = new JFishPropertyPlaceholder();
		ppc.setIgnoreResourceNotFound(true);
		ppc.setIgnoreUnresolvablePlaceholders(true);
		ppc.setSearchSystemEnvironment(searchSystemEnvironment);
		List<Resource> resources = new ArrayList<Resource>();
		for(String path : locations){
			resources.add(new ClassPathResource(path));
		}
		ppc.setLocations(resources.toArray(new Resource[resources.size()]));
		return ppc;
	}
	
	public static boolean isSpringConfPlaceholder(String value){
		if(value.indexOf("${")!=-1 && value.indexOf("}")!=-1)
			return true;
		return false;
	}
	
	public static Resource newClassPathResource(String location){
		return new ClassPathResource(location);
	}
	
	public static Properties createProperties(String location, boolean throwIfError){
		Assert.hasText(location);
		Resource res = new ClassPathResource(location);
		Properties prop = null;
		try {
			prop = PropUtils.loadProperties(res.getFile());
		} catch (IOException e) {
			if(throwIfError)
				throw new BaseException("load properties error : " + e.getMessage());
			else
				logger.error("ignore load this properties["+location+"] , error : " + e.getMessage());
		}
		return prop;
	}
	
	public static JFishProperties loadAsJFishProperties(String classpath){
		PropertiesFactoryBean pfb = SpringUtils.createPropertiesBySptring(classpath);
    	try {
			pfb.afterPropertiesSet();
			JFishProperties properties = (JFishProperties)pfb.getObject();
			return properties;
		} catch (IOException e) {
			throw new BaseException("load config error: " + e.getMessage(), e);
		}
	}

	public static PropertiesFactoryBean createPropertiesBySptring(String...classpaths) {
		return createPropertiesBySptring(new JFishProperties(), classpaths);
	}
	public static PropertiesFactoryBean createPropertiesBySptring(JFishProperties properties, String...classpaths) {
//		PropertiesFactoryBean pfb = new PropertiesFactoryBean();
		PropertiesFactoryBean pfb = new JFishPropertiesFactoryBean(properties);
		pfb.setIgnoreResourceNotFound(true);
		org.springframework.core.io.Resource[] resources = new org.springframework.core.io.Resource[classpaths.length];
		int index = 0;
		for(String classpath : classpaths){
			resources[index++] = classpath(classpath);
		}
		pfb.setLocations(resources);
		return pfb;
	}
	
	public static AutowireCapableBeanFactory findAutoWiringBeanFactory(ApplicationContext context) {
        if (context instanceof AutowireCapableBeanFactory) {
            return (AutowireCapableBeanFactory) context;
        } else if (context instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) context).getBeanFactory();
        } else if (context.getParent() != null) {
            return findAutoWiringBeanFactory(context.getParent());
        }
        return null;
    }
	
	public static BeanDefinition createBeanDefinition(Class<?> beanClass, Object...params){
		Map<String, Object> props = LangUtils.asMap(params);
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(beanClass);
		if(LangUtils.isEmpty(props))
			return bdb.getBeanDefinition();
		for(Entry<String, Object> entry : props.entrySet()){
			/*try {
				bdb.addPropertyValue(entry.getKey(), entry.getValue());
			} catch (Exception e) {
				throw new BaseException("createBeanDefinition error: " + e.getMessage());
			}*/
			bdb.addPropertyValue(entry.getKey(), entry.getValue());
		}
		return bdb.getBeanDefinition();
	}
	

	public static <T> void injectAndInitialize(ApplicationContext appContext, T bean) {
		injectAndInitialize(appContext.getAutowireCapableBeanFactory(), bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
	}

	public static <T> void injectAndInitialize(AutowireCapableBeanFactory acb, T bean, int autowireMode) {
		acb.autowireBeanProperties(bean, autowireMode, false);
		initializeBean(acb, bean, autowireMode);
	}

	public static <T> void initializeBean(AutowireCapableBeanFactory acb, T bean, int autowireMode) {
		String beanName = StringUtils.uncapitalize(bean.getClass().getSimpleName());
		acb.initializeBean(bean, beanName);
	}
	

	public static <T> T registerBean(ApplicationContext context, Class<?> beanClass, Object...params){
		return registerBean(context, StringUtils.uncapitalize(beanClass.getSimpleName()), beanClass, params);
	}
	public static <T> T registerBean(ApplicationContext context, String beanName, Class<?> beanClass, Object...params){
		registerBeanDefinition(context, beanName, beanClass, params);
		T bean = (T) context.getBean(beanName);
		if(bean==null)
			throw new BaseException("register spring bean error : " + beanClass);
		return bean;
	}
	

	public static BeanDefinition registerBeanDefinition(ApplicationContext context, String beanName, Class<?> beanClass, Object...params){
		BeanDefinitionRegistry bdr = getBeanDefinitionRegistry(context, true);
		return registerBeanDefinition(bdr, beanName, beanClass, params);
	}
	

	public static BeanDefinitionRegistry getBeanDefinitionRegistry(ApplicationContext context, boolean throwIfNull){
		BeanDefinitionRegistry bdr = null;
		if(!BeanDefinitionRegistry.class.isInstance(context)){
			BeanFactory bf = context.getAutowireCapableBeanFactory();
			if(BeanDefinitionRegistry.class.isInstance(bf))
				bdr = (BeanDefinitionRegistry) bf;
		}else{
			bdr = (BeanDefinitionRegistry) context;
		}

		if(bdr==null && throwIfNull)
			throw new BaseException("this context can not rigister spring bean : " + context);
		return bdr;
	}
	
	/****
	 * 注册bean定义
	 * @param bdr
	 * @param beanName
	 * @param beanClass
	 * @param params
	 * @return
	 */
	public static BeanDefinition registerBeanDefinition(BeanDefinitionRegistry bdr, String beanName, Class<?> beanClass, Object...params){
		if(StringUtils.isBlank(beanName)){
			beanName = StringUtils.uncapitalize(beanClass.getSimpleName());
		}
		BeanDefinition bd = createBeanDefinition(beanClass, params);
		bdr.registerBeanDefinition(beanName, bd);
		return bd;
	}
	
	/*****
	 * 获取SingletonBeanRegistry
	 * @param applicationContext
	 * @return
	 */
	public static SingletonBeanRegistry getSingletonBeanRegistry(ApplicationContext applicationContext){
		BeanFactory bf = null;
		if(applicationContext instanceof AbstractApplicationContext){
			bf = ((AbstractApplicationContext)applicationContext).getBeanFactory();
		}
		if(bf==null || !SingletonBeanRegistry.class.isInstance(bf)){
			return null;
		}
		
		SingletonBeanRegistry sbr = (SingletonBeanRegistry) bf;
		return sbr;
	}
	
	public static void registerSingleton(ApplicationContext applicationContext, String beanName, Object singletonObject){
		getSingletonBeanRegistry(applicationContext).registerSingleton(beanName, singletonObject);
	}
	
	public static Resource classpath(String path){
		return new ClassPathResource(path);
	}
	
	public static BeanWrapper newBeanWrapper(Object obj, Object...listElementTypes){
		BeanWrapper bw = null;
		if(Map.class.isInstance(obj)){
			bw = new BeanMapWrapper(obj, listElementTypes);
		}else{
			bw = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		}
		bw.setAutoGrowNestedPaths(true);
		return bw;
	}
	
	public static <T> T map2Bean(Map<String, ?> props, Class<T> beanClass){
		T bean = ReflectUtils.newInstance(beanClass);
		return setMap2Bean(props, bean);
	}
	
	public static <T> T setMap2Bean(Map<String, ?> props, T bean){
		Assert.notNull(bean);
		Assert.notNull(props);
		BeanWrapper bw = newBeanWrapper(bean);
		for(Entry<String, ?> entry : props.entrySet()){
			if(bw.isWritableProperty(entry.getKey())){
				bw.setPropertyValue(entry.getKey(), entry.getValue());
			}
		}
		return bean;
	}
	
	/*public static BeanMapWrapper newBeanMapWrapper(Object obj){
		BeanMapWrapper bw = new BeanMapWrapper(obj);
		return bw;
	}*/
	
}
