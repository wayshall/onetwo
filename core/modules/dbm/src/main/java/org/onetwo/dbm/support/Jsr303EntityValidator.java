package org.onetwo.dbm.support;

import javax.validation.Validator;

import org.onetwo.common.spring.validator.ValidatorWrapper;
import org.onetwo.dbm.mapping.EntityValidator;

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
