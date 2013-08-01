package org.onetwo.common.fish.exception;



@SuppressWarnings("serial")
public class JFishEntityVersionException extends JFishOrmException{

	public JFishEntityVersionException(Object obj) {
		super("entity["+obj+"] version has changed !");
	}

	public JFishEntityVersionException(Class<?> entityClass, Object id, Object currentVersion) {
		super("entity["+entityClass+"] version has changed, id: " + id + ", entity session version: " + currentVersion + ".");
	}

	
	protected String getBaseCode(){
		return JFishErrorCode.ORM_ERROR;
	}

}
