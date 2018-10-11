package org.onetwo.common.spring.validator;

import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

public class ValidatorWrapper {
	
	public static ValidatorWrapper wrap(Validator validator){
		Assert.notNull(validator, "validator can not be null");
		return new ValidatorWrapper(validator);
	}

	private final Validator validator;
	private final SpringValidatorAdapter adapter;

	private ValidatorWrapper(Validator validator) {
		super();
		this.validator = validator;
		this.adapter = new SpringValidatorAdapter(validator);
	}

	public Validator getValidator() {
		return validator;
	}

	public <T> void validate(T object, BindingResult bindResult, Class<?>... groups){
		/*Set<ConstraintViolation<T>> constrains = getValidator().validate(object, groups);
		ValidatorUtils.addConstraintViolations(constrains, bindResult);*/
		this.adapter.validate(object, bindResult, (Object[])groups);
	}
	
	public ValidationBindingResult validate(Object object, Class<?>... groups){
		ValidationBindingResult msger = ValidationBindingResult.create(object);
		this.validate(object, msger, groups);
		return msger;
	}

	public void throwIfValidateFailed(Object obj, Class<?>... groups){
		ValidationBindingResult validations = validate(obj, groups);
		if(validations.hasErrors()){
			throw new ValidationException(validations.getErrorMessagesAsString());
		}
	}
	
}
