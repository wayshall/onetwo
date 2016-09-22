package org.onetwo.dbm.exception;



@SuppressWarnings("serial")
public class EntityNotFoundException extends DbmException{

	public EntityNotFoundException() {
		super("entity not found error!");
	}

	public EntityNotFoundException(Object obj) {
		super("entity["+obj+"] not found !");
	}

	public EntityNotFoundException(Class<?> entityClass, Object id) {
		super("entity["+entityClass+"] not found with id : " + id);
	}

	public EntityNotFoundException(String detailMsg, Class<?> entityClass, Object id) {
		super(detailMsg + " entity["+entityClass+"] not found with id : " + id);
	}

	public EntityNotFoundException(String msg) {
		super(msg);
	}

	
	protected String getDefaultCode(){
		return JFishErrorCode.ORM_ERROR;
	}

}
