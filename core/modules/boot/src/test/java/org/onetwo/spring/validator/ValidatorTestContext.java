package org.onetwo.spring.validator;


import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Validator;

import org.onetwo.common.spring.Springs;
import org.onetwo.common.spring.validator.ContentChecker;
import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.google.common.collect.Lists;

@Configuration
public class ValidatorTestContext implements InitializingBean {

	@Autowired
	ApplicationContext applicationContext;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Springs.initApplicationIfNotInitialized(applicationContext);
	}

	@Bean
	@Autowired
	public ValidatorWrapper validatorWrapper(Validator validator){
		return ValidatorWrapper.wrap(validator);
	}
	
	@Bean
	public Validator validator() {
		Validator validator = new LocalValidatorFactoryBean();
		return validator;
	}
	
	@Bean
	public ContentChecker contentChecker() {
		return new SimpleContentChecker();
	}
	
	public static class SimpleContentChecker implements ContentChecker {
		List<String> sensitiveWrods = Lists.newArrayList("敏感词1", "敏感词2");

		@Override
		public List<String> check(String content) {
			return sensitiveWrods.stream().filter(w -> {
				return content.contains(w);
			})
			.collect(Collectors.toList());
		}
		
	}
}
