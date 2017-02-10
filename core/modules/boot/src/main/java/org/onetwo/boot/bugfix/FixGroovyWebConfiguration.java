package org.onetwo.boot.bugfix;

import java.util.Locale;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration.GroovyWebConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.groovy.GroovyMarkupView;
import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;

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
