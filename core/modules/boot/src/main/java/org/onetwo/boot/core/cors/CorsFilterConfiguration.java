package org.onetwo.boot.core.cors;

import java.util.Map;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.web.BootMvcConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * @author weishao zeng
 * @see BootMvcConfigurerAdapter#addCorsMappings
 * <br/>
 */
@Deprecated
@Configuration
@ConditionalOnProperty(name=BootJFishConfig.ENABLE_MVC_CORSFILTER)
public class CorsFilterConfiguration {
	
	@Autowired
	private BootJFishConfig jfishConfig;
	
	@Bean
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
