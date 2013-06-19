package org.onetwo.common.fish.exception;



@SuppressWarnings("serial")
public class JFishEntityVersionException extends JFishOrmException{

	public JFishEntityVersionException(Object obj) {
		super("entity["+obj+"] version has changed !");
	}

	public JFishEntityVersionException(Class<?> entityClass, Object id, Object entityVersion, Object dbversion) {
		super("entity["+entityClass+"] versioh has changed, id: " + id + ", entity version: " + entityVersion + ", db version: " + dbversion);
	}

	
	protected String getBaseCode(){
		return JFishErrorCode.ORM_ERROR;
	}

}
