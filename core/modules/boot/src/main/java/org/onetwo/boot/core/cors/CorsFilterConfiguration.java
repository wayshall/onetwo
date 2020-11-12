package org.onetwo.boot.core.cors;

import java.util.Map;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.web.BootMvcConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * 启用这个过滤器的属性是：jfish.mvc.corsFilter: true
 * 不启用的时候默认使用BootMvcConfigurerAdapter#addCorsMappings策略配置跨域：即根据jfish.mvc.cors设置
 * 但是BootMvcConfigurerAdapter#addCorsMappings配置的filter优先级比较低，当使用secrity的UsernamePasswordAuthenticationFilter时，会无效（因为还没到底mvc的filter）
 * 
 * 所以，启用这个filter配置不同之处在于这个filter的优先级更高（Ordered.HIGHEST_PRECEDENCE），在security的UsernamePasswordAuthenticationFilter之上
 * @author weishao zeng
 * @see BootMvcConfigurerAdapter#addCorsMappings
 * <br/>
 */
@Configuration
@ConditionalOnProperty(name=BootJFishConfig.ENABLE_MVC_CORSFILTER)
public class CorsFilterConfiguration {
	
	@Autowired
	private BootJFishConfig jfishConfig;
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public CorsFilter corsFilter(UrlBasedCorsConfigurationSource configSource) {
		CorsFilter corsFilter = new CorsFilter(configSource);
		return corsFilter;
	}
	
	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource(ExtCorsRegistry corsRegistry) {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.setCorsConfigurations(corsRegistry.getCorsConfigurations());
		return source;
	}
	
	@Bean
	public ExtCorsRegistry extCorsRegistry() {
		ExtCorsRegistry registry = new ExtCorsRegistry();
		BootMvcConfigurerAdapter.addCorsMappings(registry, jfishConfig.getMvc().getCors());
		return registry;
	}
	
	public static class ExtCorsRegistry extends CorsRegistry {

		public Map<String, CorsConfiguration> getCorsConfigurations() {
			return super.getCorsConfigurations();
		}
	}

}
