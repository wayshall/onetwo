package org.onetwo.boot.groovy;

import java.net.MalformedURLException;
import java.net.URL;

import org.onetwo.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import groovy.util.GroovyScriptEngine;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
public class GroovyConfiguration {
	@Autowired
	private GroovyScriptFinder groovyScriptFinder;
	
	@Bean
	public GroovyBindingFactoryBean groovyBindingFactoryBean() {
		return new GroovyBindingFactoryBean();
	}
	
	@Bean
	public GroovyScriptEngine groovyScriptEngine() {
		GroovyScriptEngine groovyScriptEngine;
		try {
			groovyScriptEngine = new GroovyScriptEngine(new URL[] {
					new URL(null, "groovy://mysql/", new GroovyResourceStreamHandler(groovyScriptFinder))
			});
		} catch (MalformedURLException e) {
			throw new ServiceException("错误的url脚本资源: " + e.getMessage());
		}
		return groovyScriptEngine;
	}
	
	@Bean
	public GroovyJdbcTemplate groovyJdbcTemplate() {
		return new GroovyJdbcTemplate();
	}

}
