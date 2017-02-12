package org.onetwo.boot.bugfix;

import java.util.Locale;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration.GroovyWebConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.groovy.GroovyMarkupView;
import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;

@Configuration
@ConditionalOnClass({ Servlet.class, LocaleContextHolder.class,
		UrlBasedViewResolver.class })
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "spring.groovy.template.enabled", matchIfMissing = true)
@ConditionalOnBean(GroovyTemplateProperties.class)
public class FixGroovyWebConfiguration extends GroovyWebConfiguration {

	public FixGroovyWebConfiguration(GroovyTemplateProperties properties) {
		super(properties);
	}
	@Bean
	@ConditionalOnMissingBean(name = "groovyMarkupViewResolver")
	public GroovyMarkupViewResolver groovyMarkupViewResolver() {
		GroovyMarkupViewResolver resolver = super.groovyMarkupViewResolver();
		resolver.setViewClass(FixGroovyMarkupView.class);
		return resolver;
	}
	
	public static class FixGroovyMarkupView extends GroovyMarkupView {
		@Override
		public boolean checkResource(Locale locale) throws Exception {
			try {
				return super.checkResource(locale);
			}
			catch (IllegalArgumentException exception) {
				return false;
			}
		}
	}
}
