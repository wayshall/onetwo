package org.onetwo.spring.validator;


import javax.validation.Validator;

import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorTestContext {


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
}
