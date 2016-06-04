package org.onetwo.common.jfishdbm.support;

import javax.validation.Validator;

import org.onetwo.common.jfishdbm.mapping.EntityValidator;
import org.onetwo.common.spring.validator.ValidatorWrapper;

public class Jsr303EntityValidator implements EntityValidator {

	private final ValidatorWrapper validatorWrapper;
	
	public Jsr303EntityValidator(Validator validator) {
		super();
		this.validatorWrapper = ValidatorWrapper.wrap(validator);
	}

	@Override
	public void validate(Object entity) {
		this.validatorWrapper.throwIfValidateFailed(entity);
	}
	
	

}
