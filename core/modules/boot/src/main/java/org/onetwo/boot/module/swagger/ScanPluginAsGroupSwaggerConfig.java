package org.onetwo.boot.module.swagger;

import java.util.Arrays;
import java.util.Collection;

import org.onetwo.boot.plugin.core.PluginManager;
import org.onetwo.boot.plugin.core.WebPlugin;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;

/****
 * 自动扫描插件，注册Docket
 * @author wayshall
 *
 */
public class ScanPluginAsGroupSwaggerConfig extends AbstractSwaggerConfig implements InitializingBean {
	
	@Autowired(required=false)
	private PluginManager pluginManager;
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		int registedCount = this.registerDockets();
		int index = registedCount;
		for(WebPlugin plugin : pluginManager.getPlugins()){
			/*Predicate<RequestHandler> predicate = RequestHandlerSelectors.basePackage(ClassUtils.getPackageName(plugin.getRootClass()));
			Collection<Predicate<RequestHandler>> predicates = Arrays.asList(predicate);
			String appName = plugin.getPluginMeta().getName();
			
			Docket docket = createDocket(index+".1 ["+appName+"]外部接口", appName, Arrays.asList(webApi(predicates)));
			SpringUtils.registerAndInitSingleton(applicationContext, appName+"Docket", docket);
			Docket innerDocket = createDocket(index+".2 ["+appName+"]内部接口", appName, Arrays.asList(notWebApi(predicates)));
			SpringUtils.registerAndInitSingleton(applicationContext, appName+"InnerDocket", innerDocket);*/
			String appName = plugin.getPluginMeta().getName();
			registerDocketsByWebApiAnnotation(index, appName, plugin.getRootClass());
			index++;
		}
	}
	
	final protected void registerDocketsByWebApiAnnotation(int index, String appName, Class<?> rootClass) {
		Predicate<RequestHandler> predicate = RequestHandlerSelectors.basePackage(ClassUtils.getPackageName(rootClass));
		Collection<Predicate<RequestHandler>> predicates = Arrays.asList(predicate);
		
		Docket docket = createDocket(index+".1 ["+appName+"]外部接口", appName, Arrays.asList(webApi(predicates)));
		SpringUtils.registerAndInitSingleton(applicationContext, appName+"Docket", docket);
		Docket innerDocket = createDocket(index+".2 ["+appName+"]内部接口", appName, Arrays.asList(notWebApi(predicates)));
		SpringUtils.registerAndInitSingleton(applicationContext, appName+"InnerDocket", innerDocket);
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
