package org.onetwo.common.jfishdbm.exception;


@SuppressWarnings("serial")
public class EntityNotSavedException extends DbmException{

	public EntityNotSavedException() {
		super("entity not save error!");
	}

	public EntityNotSavedException(Object entity) {
		super("entity["+entity+"] has not saved yet!");
	}

	public EntityNotSavedException(Class<?> mainEntityClass, String fieldName) {
		super("the field["+fieldName+"] of entity["+mainEntityClass+"] has not saved yet!");
	}

	public EntityNotSavedException(String msg) {
		super(msg);
	}

	
	protected String getDefaultCode(){
		return JFishErrorCode.ORM_ERROR;
	}

}
