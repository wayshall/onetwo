package org.onetwo.dbm.exception;



@SuppressWarnings("serial")
public class EntityVersionException extends DbmException{

	public EntityVersionException(Object obj) {
		super("entity["+obj+"] version has changed !");
	}

	public EntityVersionException(Class<?> entityClass, Object id, Object currentVersion) {
		super("entity["+entityClass+"] version has changed, id: " + id + ", entity session version: " + currentVersion + ".");
	}

	
	protected String getDefaultCode(){
		return JFishErrorCode.ORM_ERROR;
	}

}
