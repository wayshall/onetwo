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


	public static Set<String> scanAnnotationPackages(ApplicationContext applicationContext, Class<? extends Annotation> annotationClass){
		ListableBeanFactory bf = (ListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
		return scanAnnotationPackages(bf, annotationClass);
	}
	
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
