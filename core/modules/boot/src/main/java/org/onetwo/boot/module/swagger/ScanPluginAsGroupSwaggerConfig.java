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

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;

import com.google.common.base.Predicate;

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
//		int index = 0;
		for(WebPlugin plugin : pluginManager.getPlugins()){
			Predicate<RequestHandler> predicate = RequestHandlerSelectors.basePackage(ClassUtils.getPackageName(plugin.getRootClass()));
			Collection<Predicate<RequestHandler>> predicates = Arrays.asList(predicate);
			String appName = plugin.getPluginMeta().getName();
			
			Docket docket = createDocket("["+appName+"]外部接口", appName, Arrays.asList(webApi(predicates)));
			SpringUtils.registerAndInitSingleton(applicationContext, appName+"Docket", docket);
			Docket innerDocket = createDocket("["+appName+"]内部接口", appName, Arrays.asList(notWebApi(predicates)));
			SpringUtils.registerAndInitSingleton(applicationContext, appName+"InnerDocket", innerDocket);
//			index++;
		}
	}

}
