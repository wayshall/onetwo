package org.onetwo.common.db.dquery;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.dquery.repostory.AnnotationScanBasicDynamicQueryObjectRegister;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.dbm.utils.DbmUtils;
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
		Set<String> packs = new HashSet<>();
		packs.addAll(DbmUtils.scanEnableDbmPackages(beanFactory));
		packs.addAll(DbmUtils.scanDbmPackages(beanFactory));
		
		if(this.packagesToScan!=null){
			Collections.addAll(packs, this.packagesToScan);
		}
		this.packagesToScan = packs.toArray(new String[0]);

		if(ArrayUtils.isNotEmpty(packagesToScan)){
			AnnotationScanBasicDynamicQueryObjectRegister register = new AnnotationScanBasicDynamicQueryObjectRegister(registry);
			register.setPackagesToScan(packagesToScan);
			register.registerQueryBeans();
		}
	}
	
}
