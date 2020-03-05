package org.onetwo.common.spring.utils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

/**
 * @author weishao zeng
 * <br/>
 */
public class SpringAnnotationUtils {

	/***
	 * Set<String> packageNames = SpringAnnotationUtils.scanAnnotationPackages(applicationContext, EnableDbmUI.class)
	 * @author weishao zeng
	 * @param applicationContext
	 * @param annotationClass
	 * @return
	 */
	public static Set<String> scanAnnotationPackages(ApplicationContext applicationContext, Class<? extends Annotation> annotationClass){
		ListableBeanFactory bf = (ListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
		return scanAnnotationPackages(bf, annotationClass);
	}
	
	/****
	 * 首先找到标注了 annotationCLass 注解（比如 @EnableDbmUI ）的bean
	 * 然后读取注解的packagesToScan属性值，有则返回此值
	 * 没有则返回标注了注解的bean所在包，作为返回值
	 * 
	 * sample:
	 *  
	 * @EnableDbmUI
	 * class EnableDbmUIBean {
	 * }
	 * 
	 * Set<String> packageNames = SpringAnnotationUtils.scanAnnotationPackages(applicationContext, EnableDbmUI.class)
	 * packageNames = EnableDbmUIBean.class.getPackage().getName();
	 * 
	 * 此举是为了避免多个模块都使用了同一个注解，但是属性设置了不同的值，导致某些注解属性没启用的情况
	 * 
	 * @author weishao zeng
	 * @param beanFactory
	 * @param annotationCLass
	 * @return
	 */
	public static Set<String> scanAnnotationPackages(ListableBeanFactory beanFactory, Class<? extends Annotation> annotationCLass){
		Set<String> packageNames = new HashSet<>();
		SpringUtils.scanAnnotation(beanFactory, annotationCLass, (beanDef, beanClass)->{
//			Annotation enableDbm = beanClass.getAnnotation(annotationCLass);
			AnnotationAttributes attrs = AnnotatedElementUtils.getMergedAnnotationAttributes(beanClass, annotationCLass);
			if(attrs==null){
				return ;
			}
//			String[] modelPacks = enableDbm.packagesToScan();
			String[] modelPacks = attrs.getStringArray("packagesToScan");
			if(ArrayUtils.isNotEmpty(modelPacks)){
				for(String pack : modelPacks){
					packageNames.add(pack);
				}
			}else{
				String packageName = beanClass.getPackage().getName();
				if(!packageName.startsWith("org.onetwo.")){
					packageNames.add(packageName);
				}
			}
		});
		return packageNames;
	}
	
}
