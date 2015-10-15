package org.onetwo.common.jfishdbm.support;

import javax.validation.Validator;

import org.onetwo.common.jfishdbm.mapping.EntityValidator;

public class Jsr303EntityValidator implements EntityValidator {

	private final Validator validator;
	
	public Jsr303EntityValidator(Validator validator) {
		super();
		this.validator = validator;
	}

	@Override
	public void validate(Object entity) {
		this.validator.validate(entity);
	}
	
	

}
