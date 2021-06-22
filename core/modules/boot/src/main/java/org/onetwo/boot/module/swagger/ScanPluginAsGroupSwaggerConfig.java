package org.onetwo.boot.module.swagger;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.core.WebPlugin;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.ClassUtils;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;

/****
 * 自动扫描插件，并根据webApi分开外部和内部接口，每个插件注册两个docket，一个外部接口，一个内部接口
 * @author wayshall
 *
 */
public class ScanPluginAsGroupSwaggerConfig extends AbstractSwaggerConfig implements InitializingBean {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	@Autowired(required=false)
	private PluginManager pluginManager;
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		BootUtils.asyncInit(() -> {
//			initWebPlugins();
//		});
		initWebPlugins();
	}
	
	private void initWebPlugins() {
		logger.info("start init web plugins: {}", getClass().getName());
		int registedCount = this.registerDockets();
		int index = registedCount;
		
		PropertyResolver resolver = SpringUtils.getPropertyResolver(applicationContext);
		for(WebPlugin plugin : pluginManager.getPlugins()){
			/*Predicate<RequestHandler> predicate = RequestHandlerSelectors.basePackage(ClassUtils.getPackageName(plugin.getRootClass()));
			Collection<Predicate<RequestHandler>> predicates = Arrays.asList(predicate);
			String appName = plugin.getPluginMeta().getName();
			
			Docket docket = createDocket(index+".1 ["+appName+"]外部接口", appName, Arrays.asList(webApi(predicates)));
			SpringUtils.registerAndInitSingleton(applicationContext, appName+"Docket", docket);
			Docket innerDocket = createDocket(index+".2 ["+appName+"]内部接口", appName, Arrays.asList(notWebApi(predicates)));
			SpringUtils.registerAndInitSingleton(applicationContext, appName+"InnerDocket", innerDocket);*/
			String pluginName = plugin.getPluginMeta().getName();

			String propertyName = SwaggerProperties.PREFIX + "." + pluginName + ".enabled";
			boolean enabledPluginSwagger = resolver.getProperty(propertyName, boolean.class, true);
			if (!enabledPluginSwagger) {
				logger.info("ignore plugin[{}] swagger docket", pluginName, plugin.getRootClass());
				continue;
			}
			logger.info("register pluginp[{}] swagger docket for rootClass: {}", pluginName, plugin.getRootClass());
			
			registerDocketsByWebApiAnnotation(index, pluginName, plugin.getRootClass());
			index++;
		}
	}
	
	final protected Collection<Predicate<RequestHandler>> createPackagePredicateByClass(Class<?>... rootClass) {
		return Stream.of(rootClass)
					.map(c -> {
						return RequestHandlerSelectors.basePackage(ClassUtils.getPackageName(c));
					})
					.collect(Collectors.toSet());
	}
	final protected Collection<Predicate<RequestHandler>> createPackagePredicate(String... packNames) {
		return Stream.of(packNames)
					.map(packName -> {
						return RequestHandlerSelectors.basePackage(packName);
					})
					.collect(Collectors.toSet());
	}
	
	final protected void registerDocketsByWebApiAnnotation(int index, String appName, Class<?> rootClass) {
		Collection<Predicate<RequestHandler>> predicates = createPackagePredicateByClass(rootClass);
		
//		if (rootClass.getName().contains("LwrouterPlugin")) {
//			System.out.println("test");
//		}
//		Docket docket = createDocket(index+".1 ["+appName+"]外部接口", appName, Arrays.asList(webApi(predicates)));
		String docketBeanName = appName+"Docket";
		logger.info("docket[{}] created...", docketBeanName);
		this.registerDocketIfNotExist(docketBeanName, index+".1 ["+appName+"]外部接口", appName, Arrays.asList(webApi(predicates)));
		/*if (!applicationContext.containsBeanDefinition(docketBeanName)) {
			SpringUtils.registerAndInitSingleton(applicationContext, docketBeanName, docket);
			logger.info("docket[{}] registered", docketBeanName);
		} else {
			logger.info("docket[{}] ignored", docketBeanName);
		}*/
		
		docketBeanName = appName+"InnerDocket";
		logger.info("docket[{}] created...", docketBeanName);
		this.registerDocketIfNotExist(docketBeanName, index+".2 ["+appName+"]内部接口", appName, Arrays.asList(notWebApi(predicates)));
		/*Docket innerDocket = createDocket(index+".2 ["+appName+"]内部接口", appName, Arrays.asList(notWebApi(predicates)));
		if (!applicationContext.containsBeanDefinition(docketBeanName)) {
			SpringUtils.registerAndInitSingleton(applicationContext, docketBeanName, innerDocket);
			logger.info("docket[{}] registered", docketBeanName);
		} else {
			logger.info("docket[{}] ignored", docketBeanName);
		}*/
	}
	
	protected void registerDocketIfNotExist(String docketBeanName, String groupName, String appName, Collection<Predicate<RequestHandler>> packages) {
		Docket innerDocket = createDocket(groupName, appName, packages);
		if (!applicationContext.containsBeanDefinition(docketBeanName)) {
			SpringUtils.registerAndInitSingleton(applicationContext, docketBeanName, innerDocket);
			logger.info("docket[{}] registered", docketBeanName);
		} else {
			logger.info("docket[{}] ignored", docketBeanName);
		}
	}
	
	/****
	 * 在注册插件的swagger前，注册其它（比如主项目）swagger
	 * @author weishao zeng
	 * @return
	 */
	protected int registerDockets() {
		return 0;
	}

}
