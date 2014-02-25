package org.onetwo.plugins.groovy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.groovy.annotation.GroovyBean;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.scripting.groovy.GroovyScriptFactory;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;

import com.google.common.base.Joiner;

public class GroovyBeanFactory implements InitializingBean {
	protected static final String LANGUAGE = "groovy";
	protected static final int CHECK_DELAY = 1000;
	protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.groovy";
	
	private final Logger logger = MyLoggerFactory.getLogger(GroovyBeanFactory.class);

	@javax.annotation.Resource
	private GroovyPluginConfig groovyPluginConfig;
	
	@javax.annotation.Resource
	private ApplicationContext applicationContext;
	
	private JFishResourcesScanner scanner;
	
	private String[] packagesToScan;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		scanner = new JFishResourcesScanner(DEFAULT_RESOURCE_PATTERN);
		scanGroovySourceAsBean(packagesToScan);
	}

	protected void scanGroovySourceAsBean(String... packagesToScan){
		List<File> groovyFiles = scanner.scan(new ScanResourcesCallback<File>(){

			@Override
			public boolean isCandidate(MetadataReader metadataReader) {
				return metadataReader.getAnnotationMetadata().hasAnnotation(GroovyBean.class.getName());
			}

			@Override
			public File doWithCandidate(MetadataReader metadataReader, Resource resource, int count) {
				try {
					return resource.getFile();
				} catch (IOException e) {
					throw new BaseException("get file error for resouce: " + resource);
				}
			}
			
		}, packagesToScan);
		
		if(LangUtils.isEmpty(groovyFiles))
			logger.info("no groovy source found in dir : " + Joiner.on(",").join(packagesToScan));
		
		DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) applicationContext;
		for(File source : groovyFiles){
			GenericBeanDefinition gbd = new GenericBeanDefinition();
			gbd.setBeanClassName(GroovyScriptFactory.class.getName());
			gbd.setAttribute(ScriptFactoryPostProcessor.LANGUAGE_ATTRIBUTE, LANGUAGE);
			gbd.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE, CHECK_DELAY);
			gbd.getConstructorArgumentValues().addIndexedArgumentValue(0, source.getPath());
			String beanName = FileUtils.getFileNameWithoutExt(source.getPath());
			dbf.registerBeanDefinition(beanName, gbd);
			logger.info("register groovy bean : " + beanName);
		}
	}

	public void setPackagesToScan(String... packagesToScan) {
		this.packagesToScan = packagesToScan;
	}
	
	

}
