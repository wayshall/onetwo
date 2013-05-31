package org.onetwo.common.fish.exception;



@SuppressWarnings("serial")
public class JFishEntityNotFoundException extends JFishOrmException{

	public JFishEntityNotFoundException() {
		super("jfish entity not found error!");
	}

	public JFishEntityNotFoundException(Object obj) {
		super("entity["+obj+"] not found !");
	}

	public JFishEntityNotFoundException(Class<?> entityClass, Object id) {
		super("entity["+entityClass+"] not found with id : " + id);
	}

	public JFishEntityNotFoundException(String detailMsg, Class<?> entityClass, Object id) {
		super(detailMsg + " entity["+entityClass+"] not found with id : " + id);
	}

	public JFishEntityNotFoundException(String msg) {
		super(msg);
	}

	
	protected String getBaseCode(){
		return JFishErrorCode.ORM_ERROR;
	}

}
