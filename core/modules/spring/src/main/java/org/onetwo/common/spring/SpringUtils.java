package org.onetwo.common.spring;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.expr.Expression;
import org.onetwo.common.expr.ExpressionFacotry;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.propconf.JFishProperties;
import org.onetwo.common.propconf.PropUtils;
import org.onetwo.common.reflect.BeanToMapConvertor;
import org.onetwo.common.reflect.BeanToMapConvertor.BeanToMapBuilder;
import org.onetwo.common.reflect.BeanToMapConvertor.PropertyContext;
import org.onetwo.common.reflect.BeanToMapConvertor.ValuePutter;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.config.JFishPropertyPlaceholder;
import org.onetwo.common.spring.utils.BeanMapWrapper;
import org.onetwo.common.spring.utils.JFishPropertiesFactoryBean;
import org.onetwo.common.spring.utils.MapToBeanConvertor;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
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
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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

	private static final MapToBeanConvertor MAP_TO_BEAN = MapToBeanConvertor.builder().build();
	
	private static final FormattingConversionService CONVERSION_SERVICE = new DefaultFormattingConversionService();
	private static final BeanToMapConvertor BEAN_TO_MAP_CONVERTOR = BeanToMapBuilder.newBuilder()
																			.enableFieldNameAnnotation()
																			.build();
	public static final Expression DOLOR = ExpressionFacotry.newExpression("${", "}");
	
	private SpringUtils(){
	}

	public static FormattingConversionService getFormattingConversionService(){
		return CONVERSION_SERVICE;
	}
	

	public static <T> T convertValue(Object value, Class<T> targetType){
    	T res = (T)getFormattingConversionService().convert(value, TypeDescriptor.valueOf(value.getClass()), TypeDescriptor.valueOf(targetType));
    	return res;
	}
	
	public static Object convertValue(Object value, Class<?> clazz, String name, boolean byProperty){
		TypeDescriptor td = byProperty?typeDescriptorForPerperty(clazz, name):typeDescriptorForField(clazz, name);
    	Object res = getFormattingConversionService().convert(value, TypeDescriptor.valueOf(value.getClass()), td);
    	return res;
	}
	
	/*public static Map<String, Object> toFlatMap(Object obj) {
		return toFlatMap(obj, o->!LangUtils.isSimpleTypeObject(o));
	}*/
	
	public static Map<String, Object> toFlatMap(Object obj) {
		Map<String, Object> map = Maps.newHashMap();
		
		ConsumableValuePutter putter = new ConsumableValuePutter((k, v) -> {
			map.put(k, v);
		});
		/*BEAN_TO_MAP_CONVERTOR.flatObject("", obj, (k, v, ctx)->{
			Object value = v;
			TypeDescriptor sourceType = null;
			if(v.getClass()!=String.class){
            	Field field = ctx.getField();
            	if(field!=null && (field.getAnnotation(DateTimeFormat.class)!=null || field.getAnnotation(NumberFormat.class)!=null) ){
    	            sourceType = new TypeDescriptor(field);
            	}
            	if(sourceType==null){
    	            sourceType = new TypeDescriptor(new Property(ctx.getSource().getClass(), ctx.getProperty().getReadMethod(), ctx.getProperty().getWriteMethod()));
            	}
            	value = getFormattingConversionService().convert(v, sourceType, TypeDescriptor.valueOf(String.class));
            }
        	map.put(k, value);
		});*/
		BEAN_TO_MAP_CONVERTOR.flatObject("", obj, putter);
		return map;
	}
	
	public static class ConsumableValuePutter implements ValuePutter {
		private final BiConsumer<String, Object> consumer;
		
		public ConsumableValuePutter(BiConsumer<String, Object> consumer) {
			super();
			this.consumer = consumer;
		}

		@Override
		public void put(String k, Object v, PropertyContext ctx) {
			Object value = v;
			TypeDescriptor sourceType = null;
			if(v.getClass()!=String.class){
            	Field field = ctx.getField();
            	if(field!=null && (field.getAnnotation(DateTimeFormat.class)!=null || field.getAnnotation(NumberFormat.class)!=null) ){
    	            sourceType = new TypeDescriptor(field);
            	}
            	if(sourceType==null){
    	            sourceType = new TypeDescriptor(new Property(ctx.getSource().getClass(), ctx.getProperty().getReadMethod(), ctx.getProperty().getWriteMethod()));
            	}
            	value = getFormattingConversionService().convert(v, sourceType, TypeDescriptor.valueOf(String.class));
            }
//        	map.put(k, value);
			this.consumer.accept(k, value);
		
		}
		
	}
	
	public static TypeDescriptor typeDescriptorForPerperty(Class<?> clazz, String propertyName){
		PropertyDescriptor pd = ReflectUtils.getPropertyDescriptor(clazz, propertyName);
		TypeDescriptor td = new TypeDescriptor(new Property(clazz, pd.getReadMethod(), pd.getWriteMethod()));
		return td;
	}
	
	public static TypeDescriptor typeDescriptorForField(Class<?> clazz, String fieldName){
    	Field field = ReflectUtils.findField(clazz, fieldName);
		TypeDescriptor td = new TypeDescriptor(field);
		return td;
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
	
	public static class WithAnnotationBeanData<T extends Annotation> {
		final private T annotation;
		final private String name;
		final private Object bean;
		public WithAnnotationBeanData(T annotation, String name, Object bean) {
			super();
			this.annotation = annotation;
			this.name = name;
			this.bean = bean;
		}
		public T getAnnotation() {
			return annotation;
		}
		public String getName() {
			return name;
		}
		public Object getBean() {
			return bean;
		}
		
	}
	public static <T extends Annotation> List<WithAnnotationBeanData<T>> getBeansWithAnnotation(ApplicationContext applicationContext, Class<T> annotationType){
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(annotationType);
		return beans.entrySet().stream().map(e->{
			T annotation = AnnotationUtils.findAnnotation(e.getValue().getClass(), annotationType);
			WithAnnotationBeanData<T> data = new WithAnnotationBeanData<T>(annotation, e.getKey(), e.getValue());
			return data;
		})
		.collect(Collectors.toList());
	}

	public static <T> List<T> getBeans(ListableBeanFactory appContext, Class<T> clazz) {
		Map<String, T> beanMaps = BeanFactoryUtils.beansOfTypeIncludingAncestors(appContext, clazz);
		if(beanMaps==null || beanMaps.isEmpty())
			return Collections.emptyList();
		List<T> list = new ArrayList<T>(beanMaps.values());
		AnnotationAwareOrderComparator.sort(list);
		return list;
	}
	public static <T> List<T> getBeans(ListableBeanFactory appContext, Class<?> clazz, ParameterizedTypeReference<T> typeRef) {
		List<T> list = (List<T>)getBeans(appContext, clazz);
		return list;
	}
	
	public static <T> Map<String, T> getBeansAsMap(ApplicationContext appContext, Class<T> clazz) {
		Map<String, T> beanMaps = BeanFactoryUtils.beansOfTypeIncludingAncestors(appContext, clazz);
		return beanMaps;
	}
	
	public static <T> T getBean(ListableBeanFactory appContext, Class<T> clazz) {
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
	
	public static Resource newPathResource(String location){
		Assert.hasText(location);
		Resource resource = null;
		if(location.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)){
			String classpath = StringUtils.substringAfter(location, ResourceUtils.CLASSPATH_URL_PREFIX);
			resource = new ClassPathResource(classpath);
		}else{
			resource = new FileSystemResource(location);
		}
		return resource;
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
	
	public static JFishProperties loadAsJFishProperties(String... classpaths){
		PropertiesFactoryBean pfb = createPropertiesBySptring(classpaths);
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
	
	public static BeanDefinition createBeanDefinition(Class<?> beanClass, Object[] constructorArgs, Map<String, Object> properties){
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(beanClass);
		if(constructorArgs!=null){
			for(Object arg : constructorArgs){
				bdb.addConstructorArgValue(arg);
			}
		}
		if(LangUtils.isNotEmpty(properties)){
			for(Entry<String, Object> entry : properties.entrySet()){
				bdb.addPropertyValue(entry.getKey(), entry.getValue());
			}
		}
		return bdb.getBeanDefinition();
	}
	

	public static <T> void autowireBean(ApplicationContext appContext, T existingBean){
		appContext.getAutowireCapableBeanFactory().autowireBean(existingBean);
	}

	public static <T> void injectAndInitialize(ApplicationContext appContext, T bean) {
		injectAndInitialize(appContext.getAutowireCapableBeanFactory(), bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
	}

	public static <T> void injectAndInitialize(AutowireCapableBeanFactory acb, T bean, int autowireMode) {
		acb.autowireBeanProperties(bean, autowireMode, false);
		initializeBean(acb, bean);
	}

	public static <T> void initializeBean(AutowireCapableBeanFactory acb, T bean) {
		String beanName = StringUtils.uncapitalize(bean.getClass().getSimpleName());
		acb.initializeBean(bean, beanName);
	}

	public static <T> void initializeBean(ApplicationContext appContext, T bean) {
		String beanName = StringUtils.uncapitalize(bean.getClass().getSimpleName());
		appContext.getAutowireCapableBeanFactory().initializeBean(bean, beanName);
	}
	

	public static <T> T registerBean(BeanFactory context, Class<?> beanClass, Object...params){
		return registerBean(context, StringUtils.uncapitalize(beanClass.getSimpleName()), beanClass, params);
	}
	
	/***
	 * register and get bean
	 * @author wayshall
	 * @param context
	 * @param beanName
	 * @param beanClass
	 * @param params
	 * @return
	 */
	public static <T> T registerBean(BeanFactory context, String beanName, Class<?> beanClass, Object...params){
		registerBeanDefinition(context, beanName, beanClass, params);
		T bean = (T) context.getBean(beanName);
		if(bean==null)
			throw new BaseException("register spring bean error : " + beanClass);
		return bean;
	}
	

	/*public static BeanDefinition registerBeanDefinition(ApplicationContext context, String beanName, Class<?> beanClass, Object...params){
		BeanDefinitionRegistry bdr = getBeanDefinitionRegistry(context, true);
		return registerBeanDefinition(bdr, beanName, beanClass, params);
	}
	public static BeanDefinition registerBeanDefinition(BeanFactory context, String beanName, Class<?> beanClass, Object...params){
		BeanDefinitionRegistry bdr = getBeanDefinitionRegistry(context, true);
		return registerBeanDefinition(bdr, beanName, beanClass, params);
	}*/
	

	/*public static BeanDefinitionRegistry getBeanDefinitionRegistry(Object context, boolean throwIfNull){
		BeanDefinitionRegistry bdr = null;
		if(ApplicationContext.class.isInstance(context)){
			BeanFactory bf = ((ApplicationContext)context).getAutowireCapableBeanFactory();
			if(BeanDefinitionRegistry.class.isInstance(bf))
				bdr = (BeanDefinitionRegistry) bf;
		}else if(BeanDefinitionRegistry.class.isInstance(context)){
			bdr = (BeanDefinitionRegistry) context;
		}else{
			//
		}

		if(bdr==null && throwIfNull)
			throw new BaseException("this context can not rigister spring bean : " + context);
		return bdr;
	}*/
	
	/****
	 * 注册bean定义
	 * @param bdr
	 * @param beanName
	 * @param beanClass
	 * @param params
	 * @return
	 */
	public static BeanDefinition registerBeanDefinition(Object context, String beanName, Class<?> beanClass, Object...params){
		BeanDefinitionRegistry bdr = getBeanDefinitionRegistry(context, true);
		if(StringUtils.isBlank(beanName)){
			beanName = StringUtils.uncapitalize(beanClass.getSimpleName());
		}
		BeanDefinition bd = createBeanDefinition(beanClass, null, LangUtils.asMap(params));
		bdr.registerBeanDefinition(beanName, bd);
		return bd;
	}
	
	/*****
	 * 获取SingletonBeanRegistry
	 * @param applicationContext
	 * @return
	 */
	public static SingletonBeanRegistry getSingletonBeanRegistry(Object applicationContext){
		Object bf = applicationContext;
		if(applicationContext instanceof AbstractApplicationContext){
			bf = ((AbstractApplicationContext)applicationContext).getBeanFactory();
		}
		if(bf==null || !SingletonBeanRegistry.class.isInstance(bf)){
			return null;
		}
		SingletonBeanRegistry sbr = (SingletonBeanRegistry) bf;
		return sbr;
	}
	public static BeanDefinitionRegistry getBeanDefinitionRegistry(Object applicationContext){
		return getBeanDefinitionRegistry(applicationContext, true);
	}
	public static BeanDefinitionRegistry getBeanDefinitionRegistry(Object applicationContext, boolean throwIfNull){
		if(BeanDefinitionRegistry.class.isInstance(applicationContext)){
			return (BeanDefinitionRegistry) applicationContext;
		}
		Object bf = applicationContext;
		if(applicationContext instanceof AbstractApplicationContext){
			bf = ((AbstractApplicationContext)applicationContext).getBeanFactory();
		}
		if(bf==null || !BeanDefinitionRegistry.class.isInstance(bf)){
			if(throwIfNull){
				throw new BaseException("this context can not rigister spring bean : " + applicationContext);
			}else{
				return null;
			}
		}
		BeanDefinitionRegistry sbr = (BeanDefinitionRegistry) bf;
		return sbr;
	}
	
	public static void registerSingleton(BeanFactory applicationContext, String beanName, Object singletonObject){
		getSingletonBeanRegistry(applicationContext).registerSingleton(beanName, singletonObject);
	}
	
	public static void registerAndInitSingleton(ApplicationContext applicationContext, String beanName, Object singletonObject){
		getSingletonBeanRegistry(applicationContext).registerSingleton(beanName, singletonObject);
		injectAndInitialize(applicationContext, singletonObject);
	}
	
	public static Resource classpath(String path){
		return new ClassPathResource(path);
	}
	
	public static InputStream classpathStream(String path){
		try {
			return classpath(path).getInputStream();
		} catch (IOException e) {
			throw new BaseException("read classpath file as stream error: " + path);
		}
	}
	
	public static BeanWrapper newBeanWrapper(Object obj){
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		bw.setAutoGrowNestedPaths(true);
		return bw;
	}
	
	public static ConfigurablePropertyAccessor newPropertyAccessor(Object obj, boolean directFieldAccess){
		ConfigurablePropertyAccessor bw = directFieldAccess?PropertyAccessorFactory.forDirectFieldAccess(obj):PropertyAccessorFactory.forBeanPropertyAccess(obj);
		bw.setAutoGrowNestedPaths(true);
		return bw;
	}
	
	public static BeanWrapper newBeanMapWrapper(Object obj, Object...listElementTypes){
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
	

	public static Map<String, Object> beansOfAnnotationIncludingAncestors(ListableBeanFactory lbf, Class<? extends Annotation> annotationType)
			throws BeansException {

		Assert.notNull(lbf, "ListableBeanFactory must not be null");
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.putAll(lbf.getBeansWithAnnotation(annotationType));
		if (lbf instanceof HierarchicalBeanFactory) {
			HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
			if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
				Map<String, Object> parentResult = beansOfAnnotationIncludingAncestors(
						(ListableBeanFactory) hbf.getParentBeanFactory(), annotationType);
				for (Map.Entry<String, Object> entry : parentResult.entrySet()) {
					String beanName = entry.getKey();
					if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
						result.put(beanName, entry.getValue());
					}
				}
			}
		}
		return result;
	}
	
	public static String[] getConstructorNames(Class<?> clazz){
		return getConstructorNames(clazz, 0);
	}
	public static String[] getConstructorNames(Class<?> clazz, int constructorIndex){
		Constructor<?> targetConstructor = ReflectUtils.getConstructor(clazz, constructorIndex);
		return getConstructorNames(targetConstructor);
	}
	public static String[] getConstructorNames(Constructor<?> targetConstructor){
		LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
		return discoverer.getParameterNames(targetConstructor);
	}
	

	public static Class<?> getTargetClass(Object candidate) {
		Class<?> targetClass = AopUtils.getTargetClass(candidate);
		if(ClassUtils.isCglibProxyClass(targetClass)){
			targetClass = candidate.getClass().getSuperclass();
		}
		return targetClass;
	}
	
	public static boolean scanAnnotation(ApplicationContext applicationContext, Class<? extends Annotation> annoClass, BiConsumer<Object, Class<?>> consumer){
		ListableBeanFactory beanFactory = (ListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
		return scanAnnotation(beanFactory, annoClass, consumer);
	}
	/****
	 * 
	 * @author wayshall
	 * @param beanFactory
	 * @param annoClass
	 * @param consumer
	 * @return 如果找到指定注解的bean，则返回true，否则返回false
	 */
	public static boolean scanAnnotation(ListableBeanFactory beanFactory, Class<? extends Annotation> annoClass, BiConsumer<Object, Class<?>> consumer){
		String[] beanNames = beanFactory.getBeanNamesForAnnotation(annoClass);
		for(String beanName : beanNames){
			Object bean = beanFactory.getBean(beanName);
			Class<?> beanClass = SpringUtils.getTargetClass(bean);
			consumer.accept(bean, beanClass);
		}
		return ArrayUtils.isNotEmpty(beanNames);
	}
	
	public static AnnotationAttributes getAnnotationAttributes(AnnotatedTypeMetadata metadata, Class<?> annoType){
		//support @AliasFor
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annoType.getName(), false));
		return attributes;
	}

	public static Optional<AnnotationAttributes> getAnnotationAttributes(Method method, Class<? extends Annotation> annoClass, boolean searchOnClass){
		AnnotationAttributes attrs = null;
		if(searchOnClass){
			attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(method, annoClass);
			if(attrs==null){
				Class<?> clazz = method.getDeclaringClass();
				attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(clazz, annoClass);
			}
		}else{
			attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(method, annoClass);
		}
		return Optional.ofNullable(attrs);
	}
	public static Optional<AnnotationAttributes> getAnnotationAttributes(Method method, Class<? extends Annotation> annoClass){
		return getAnnotationAttributes(method, annoClass, false);
	}
	
	/***
	 * 
	 * @author wayshall
	 * @param applicationContext
	 * @param value
	 * @return
	 */
	public static String resolvePlaceholders(Object applicationContext, String value){
//		return resolvePlaceholders(applicationContext, value, true);
		String resolvedValue = resolvePlaceholders(applicationContext, value, false);
		if (DOLOR.isExpresstion(resolvedValue)){
			String convertedName = StringUtils.convertWithSeperator(resolvedValue, "-");
			if (value.equals(convertedName)) {
				throw new BaseException("can not resolve placeholders value: " + value + ", resovled value: " + resolvedValue);
			}
			resolvedValue = resolvePlaceholders(applicationContext, convertedName, false);
			if (DOLOR.isExpresstion(resolvedValue)){
				throw new BaseException("can not resolve placeholders value: " + value + ", resovled value: " + resolvedValue);
			}
		}
		return resolvedValue;
	}
	
	public static String resolvePlaceholders(Object applicationContext, String value, boolean throwIfNotResolved){
		String newValue = value;
		if (StringUtils.hasText(value) && DOLOR.isExpresstion(value)){
			if (applicationContext instanceof ConfigurableApplicationContext){
				ConfigurableApplicationContext appcontext = (ConfigurableApplicationContext)applicationContext;
				newValue = appcontext.getEnvironment().resolvePlaceholders(value);
			} else if (applicationContext instanceof PropertyResolver){
				PropertyResolver env = (PropertyResolver)applicationContext;
				newValue = env.resolvePlaceholders(value);
			}
			if (DOLOR.isExpresstion(newValue) && throwIfNotResolved){
				throw new BaseException("can not resolve placeholders value: " + value + ", resovled value: " + newValue);
			}
		}
		return newValue;
	}
	
	public static <T> T toBean(Map<String, ?> propValues, Class<T> beanClass){
		return MAP_TO_BEAN.toBean(propValues, beanClass);
	}

	public static MapToBeanConvertor getMapToBean() {
		return MAP_TO_BEAN;
	}
	
	public static String readClassPathFile(String path){
		return readClassPathFile(path, FileUtils.DEFAULT_CHARSET);
	}
	
	public static String readClassPathFile(String path, String charset){
		Resource res = classpath(path);
		try {
			return IOUtils.toString(res.getInputStream(), charset);
		} catch (IOException e) {
			throw new BaseException("read classpath file error: " + path);
		}
	}
	
	public static String readMultipartFile(MultipartFile multipartFile){
		return readMultipartFile(multipartFile, FileUtils.DEFAULT_CHARSET);
	}
	
	public static String readMultipartFile(MultipartFile multipartFile, String charset){
		try {
			return IOUtils.toString(multipartFile.getInputStream(), charset);
		} catch (IOException e) {
			throw new BaseException("read classpath file error: " + multipartFile.getOriginalFilename());
		}
	}
	
	public static InputStream getInputStream(Resource resource){
		try {
			return resource.getInputStream();
		} catch (IOException e) {
			throw new BaseException("get InputStream error: " + resource.getFilename());
		}
	}
	

	public static BeanToMapConvertor getBeanToMapConvertor(String... excludeProperties){
		List<String> excludes = Lists.newArrayList();
		excludes.addAll(Arrays.asList(excludeProperties));
		BeanToMapConvertor convertor = BeanToMapBuilder.newBuilder()
														.ignoreNull()
														.excludeProperties(excludes.toArray(new String[0]))
														.enableFieldNameAnnotation()
														.build();
		return convertor;
	}
	
}
