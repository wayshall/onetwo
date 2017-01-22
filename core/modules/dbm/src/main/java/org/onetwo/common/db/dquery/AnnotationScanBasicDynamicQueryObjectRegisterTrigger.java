package org.onetwo.common.db.dquery;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.dquery.annotation.QueryRepositoryPackages;
import org.onetwo.common.db.dquery.repostory.AnnotationScanBasicDynamicQueryObjectRegister;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.dbm.spring.EnableDbm;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

public class AnnotationScanBasicDynamicQueryObjectRegisterTrigger implements BeanFactoryPostProcessor {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private BeanDefinitionRegistry registry;
//	private ApplicationContext applicationContext;
	private String[] packagesToScan;
	
	public AnnotationScanBasicDynamicQueryObjectRegisterTrigger(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}

	public AnnotationScanBasicDynamicQueryObjectRegisterTrigger(ApplicationContext applicationContext) {
		this.registry = SpringUtils.getBeanDefinitionRegistry(applicationContext);
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		List<String> packs = new ArrayList<>();
		this.scanAnnotationPackages(beanFactory, EnableDbm.class, (beanDef, beanClass)->{
			String packageName = beanClass.getPackage().getName();
			if(!packageName.startsWith("org.onetwo")){
				packs.add(packageName);
			}
		});
		this.scanAnnotationPackages(beanFactory, QueryRepositoryPackages.class, (beanDef, beanClass)->{
			QueryRepositoryPackages qrp = beanClass.getAnnotation(QueryRepositoryPackages.class);
			Stream.of(qrp.value()).forEach(p->{
				packs.add(p);
			});
		});
		if(!packs.isEmpty()){
			if(this.packagesToScan!=null){
				Collections.addAll(packs, this.packagesToScan);
			}
			this.packagesToScan = packs.toArray(new String[0]);
		}

		if(ArrayUtils.isNotEmpty(packagesToScan)){
			AnnotationScanBasicDynamicQueryObjectRegister register = new AnnotationScanBasicDynamicQueryObjectRegister(registry);
			register.setPackagesToScan(packagesToScan);
			register.registerQueryBeans();
		}
	}
	
	private void scanAnnotationPackages(ConfigurableListableBeanFactory beanFactory, Class<? extends Annotation> annoClass, BiConsumer<Object, Class<?>> consumer){
		String[] beanNames = beanFactory.getBeanNamesForAnnotation(annoClass);
		for(String beanName : beanNames){
			Object bean = beanFactory.getBean(beanName);
			Class<?> beanClass = SpringUtils.getTargetClass(bean);
			consumer.accept(bean, beanClass);
		}
	}

}
