package org.onetwo.boot.core.web;

import org.onetwo.boot.core.BootContextConfig;
import org.onetwo.boot.core.BootWebCommonAutoConfig;
import org.onetwo.boot.core.config.BootBusinessConfig;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSpringConfig;
import org.onetwo.boot.core.embedded.TomcatProperties;
import org.onetwo.boot.core.web.mvc.exception.BootWebExceptionResolver;
import org.onetwo.boot.core.web.view.BootJsonView;
import org.onetwo.boot.core.web.view.ViewResolverConfiguration;
import org.onetwo.boot.plugin.PluginContextConfig;
import org.onetwo.boot.plugin.ftl.WebFtlsContextConfig;
import org.onetwo.common.web.init.CommonWebFilterInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/****
 * ui模式扩展配置
 * 如自定义的corsfilter， PluginContextConfig
 * @author wayshall
 *
 */
@Configuration
//@EnableConfigurationProperties({JFishBootConfig.class, SpringBootConfig.class})
//@EnableConfigurationProperties({HttpEncodingProperties.class, BootJFishConfig.class, BootSpringConfig.class, BootBusinessConfig.class, BootSiteConfig.class, TomcatProperties.class})
@EnableConfigurationProperties({ServerProperties.class, BootJFishConfig.class, BootSpringConfig.class, BootBusinessConfig.class, BootSiteConfig.class, TomcatProperties.class})
@Import({BootContextConfig.class, 
		ViewResolverConfiguration.class,
		PluginContextConfig.class, 
		WebFtlsContextConfig.class})
//@ConditionalOnProperty(name=BootJFishConfig.ENABLE_JFISH_AUTO_CONFIG, havingValue=BootJFishConfig.VALUE_AUTO_CONFIG_WEB_UI, matchIfMissing=true)
@ConditionalOnClass(CommonWebFilterInitializer.class)
public class BootWebUIContextAutoConfig extends BootWebCommonAutoConfig {
//	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	/*@Autowired
	private HttpEncodingProperties httpEncodingProperties;*/
	
	public BootWebUIContextAutoConfig(){
	}
	/*@Bean
	public BootSiteConfig bootSiteConfig(){
		return bootSiteConfig;
	}*/

	/***
	 * 异常解释
	 * BootWebExceptionHandler 无法拦截在spring拦截器里抛出的异常，所以这里配置自定义的HandlerExceptionResolver进行拦截
	 * @return
	 */
	@Bean(BootWebCommonAutoConfig.BEAN_NAME_EXCEPTION_RESOLVER)
//	@ConditionalOnMissingBean({BootWebExceptionResolver.class, ResponseEntityExceptionHandler.class})
//	@Autowired
	@ConditionalOnMissingBean({BootWebExceptionResolver.class})
	public BootWebExceptionResolver bootWebExceptionResolver(BootJsonView jsonView){
		BootWebExceptionResolver resolver = new BootWebExceptionResolver();
//		resolver.setExceptionMessage(exceptionMessage);
		resolver.setJfishConfig(bootJfishConfig);
		resolver.setErrorView(jsonView);
		return resolver;
	}

	/***
	 * instead of boot mapper config by JacksonAutoConfiguration
	 * @return
	 */
	/*@Bean
	public ObjectMapper objectMapper(){
		return BootWebUtils.createObjectMapper(applicationContext);
	}*/
	
	/*@Bean
	public WebHolderManager webHolderManager() {
		WebHolderManager webHolderManager = new WebHolderManager();
		return webHolderManager;
	}*/
	

}
