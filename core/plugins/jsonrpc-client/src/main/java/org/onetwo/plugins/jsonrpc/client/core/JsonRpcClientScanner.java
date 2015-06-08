package org.onetwo.plugins.jsonrpc.client.core;

import java.util.List;

import org.onetwo.common.jsonrpc.annotation.JsonRpcService;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.Assert;


public class JsonRpcClientScanner implements BeanDefinitionRegistryPostProcessor {

	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
//	private final ApplicationContext applicationContext;
	
	private JFishResourcesScanner scanner = new JFishResourcesScanner();
	private String[] packagesToScan;
	private String serverEndpoint;

	public JsonRpcClientScanner(String serverEndpoint, String... packagesToScan) {
		super();
//		Assert.notNull(applicationContext);
//		this.applicationContext = applicationContext;
		this.packagesToScan = packagesToScan;
		this.serverEndpoint = serverEndpoint;
	}
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		RpcClientFacotry rpcCactory = new RpcClientFacotry.Builder()
											.serverEndpoint(serverEndpoint)
											.build();
//		Assert.notNull(applicationContext);
		Assert.hasText(serverEndpoint);
		Assert.notEmpty(packagesToScan);
		List<Class<?>> rpcClientClasses = scanner.scan(new ScanResourcesCallback<Class<?>>() {
			@Override
			public Class<?> doWithCandidate(MetadataReader metadataReader, Resource resource, int count) {
				if(!metadataReader.getAnnotationMetadata().hasAnnotation(JsonRpcService.class.getName())){
					return null;
				}
				Class<?> cls = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName(), false);
				if(!cls.isInterface()){
					return null;
				}
				return cls;
			}
		}, packagesToScan);

		rpcClientClasses.stream().forEach(cls->{
			Object clientObj = rpcCactory.create(cls);
			String beanName = StringUtils.uncapitalize(cls.getSimpleName());
			logger.info("beanFactory:{}, clientObj:{}", beanFactory, clientObj);
			SpringUtils.registerSingleton(beanFactory, beanName, clientObj);
			logger.info("find JsonRpcService and registered: {} - {}", beanName, cls);
		});
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public void setServerEndpoint(String serverEndpoint) {
		this.serverEndpoint = serverEndpoint;
	}
	
}
