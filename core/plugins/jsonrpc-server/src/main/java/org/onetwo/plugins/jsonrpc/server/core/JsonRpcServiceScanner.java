package org.onetwo.plugins.jsonrpc.server.core;

import java.util.List;
import java.util.Map;

import org.javatuples.Pair;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.plugins.jsonrpc.server.annotation.JsonRpcService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.Assert;

public class JsonRpcServiceScanner implements InitializingBean {

//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private final ApplicationContext applicationContext;
	
	private JsonRpcSerivceRepository jsonRpcServiceRepository;
	private JFishResourcesScanner scanner = new JFishResourcesScanner();
	private String[] packagesToScan;

	public JsonRpcServiceScanner(ApplicationContext applicationContext) {
		super();
		Assert.notNull(applicationContext);
		this.applicationContext = applicationContext;
	}

	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(applicationContext);
		Assert.notNull(jsonRpcServiceRepository);
		Assert.notEmpty(packagesToScan);
		
		Map<String, JsonRpcSerivceListener> listeners = SpringUtils.getBeansAsMap(applicationContext, JsonRpcSerivceListener.class);
		listeners.forEach((name, listener)->{
			jsonRpcServiceRepository.registerListener(listener);
		});
		
		List<Class<?>> rpcServiceClasses = scanner.scan(new ScanResourcesCallback<Class<?>>() {
			@Override
			public Class<?> doWithCandidate(MetadataReader metadataReader, Resource resource, int count) {
				if(!metadataReader.getAnnotationMetadata().hasAnnotation(JsonRpcService.class.getName())){
					return null;
				}
				Class<?> cls = ReflectUtils.loadClass(metadataReader.getClassMetadata().getClassName(), false);
				return cls;
			}
		}, packagesToScan);

		rpcServiceClasses.stream().map((cls)->{
				List<?> beans = SpringUtils.getBeans(applicationContext, cls);
				if(beans.isEmpty())
					throw new BaseException("no rpc service implementor found for interface: " + cls);
				if(beans.size()>1)
					throw new BaseException("one more rpc service implementor found for interface: " + cls);
				return new Pair<Class<?>, Object>(cls, beans.get(0));
			})
			.forEach((Pair<Class<?>, Object> serviceMap)->jsonRpcServiceRepository.registerService(serviceMap.getValue0().getName(), serviceMap.getValue1()));
	}

	public void setJsonRpcSerivceRepository(JsonRpcSerivceRepository jsonRpcSerivceRepository) {
		this.jsonRpcServiceRepository = jsonRpcSerivceRepository;
	}


	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}
	
}
