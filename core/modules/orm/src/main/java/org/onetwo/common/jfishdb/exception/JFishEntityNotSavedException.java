package org.onetwo.common.jfishdb.exception;


@SuppressWarnings("serial")
public class JFishEntityNotSavedException extends JFishOrmException{

	public JFishEntityNotSavedException() {
		super("jfish entity not save error!");
	}

	public JFishEntityNotSavedException(Object entity) {
		super("entity["+entity+"] has not saved yet!");
	}

	public JFishEntityNotSavedException(Class<?> mainEntityClass, String fieldName) {
		super("the field["+fieldName+"] of entity["+mainEntityClass+"] has not saved yet!");
	}

	public JFishEntityNotSavedException(String msg) {
		super(msg);
	}

	
	protected String getDefaultCode(){
		return JFishErrorCode.ORM_ERROR;
	}

}
