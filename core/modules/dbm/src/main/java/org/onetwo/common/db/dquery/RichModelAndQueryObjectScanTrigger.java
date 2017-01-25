package org.onetwo.common.db.dquery;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.dquery.repostory.AnnotationScanBasicDynamicQueryObjectRegister;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.ResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.dbm.mapping.ScanedClassContext;
import org.onetwo.dbm.richmodel.PackageScanedProcessor;
import org.onetwo.dbm.richmodel.RichModel;
import org.onetwo.dbm.richmodel.RichModelCheckProcessor;
import org.onetwo.dbm.richmodel.RichModelEnhanceProcessor;
import org.onetwo.dbm.utils.DbmUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;

public class RichModelAndQueryObjectScanTrigger implements BeanFactoryPostProcessor {
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	private final ResourcesScanner scanner = ResourcesScanner.CLASS_CANNER;
	private BeanDefinitionRegistry registry;
//	private ApplicationContext applicationContext;
	private String[] packagesToScan;
	
	public RichModelAndQueryObjectScanTrigger(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}

	public RichModelAndQueryObjectScanTrigger(ApplicationContext applicationContext) {
		this.registry = SpringUtils.getBeanDefinitionRegistry(applicationContext);
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Collection<String> packs = DbmUtils.getAllDbmPackageNames(beanFactory);
		
		if(this.packagesToScan!=null){
			Collections.addAll(packs, this.packagesToScan);
		}
		this.packagesToScan = packs.toArray(new String[0]);

		if(ArrayUtils.isNotEmpty(packagesToScan)){
			
			this.enhanceRichModel();
			
			AnnotationScanBasicDynamicQueryObjectRegister register = new AnnotationScanBasicDynamicQueryObjectRegister(registry);
			register.setPackagesToScan(packagesToScan);
			register.registerQueryBeans();
		}
	}
	

	protected void enhanceRichModel(){
		PackageScanedProcessor processor = null;
		Collection<ScanedClassContext> richModels = this.scanRichModelClasses();
		if(ClassUtils.isPresent("javassist.ClassPool", null)){
			processor = new RichModelEnhanceProcessor();
		}else{
			processor = new RichModelCheckProcessor();
		}
		processor.processClasses(richModels);
	}
	protected Collection<ScanedClassContext> scanRichModelClasses(){
		Collection<ScanedClassContext> entryClassNameList = scanner.scan(new ScanResourcesCallback<ScanedClassContext>() {

			@Override
			public ScanedClassContext doWithCandidate(MetadataReader metadataReader, Resource resource, int count) {
				ScanedClassContext cls = new ScanedClassContext(metadataReader);
				//暂时忽略非richmodel
				if(cls.isSubClassOf(RichModel.class.getName())){
					return cls;
				}
				return null;
			}

		}, packagesToScan);
		return entryClassNameList;
	}
	
}
