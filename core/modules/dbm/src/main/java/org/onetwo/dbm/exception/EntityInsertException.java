package org.onetwo.dbm.exception;



@SuppressWarnings("serial")
public class EntityInsertException extends UpdateCountException{

	final private Object entity;
	
	public EntityInsertException(Object obj, int expectCount, int effectiveCount) {
		super("entity["+obj+"] insert error.", expectCount, effectiveCount);
		this.entity = obj;
	}

	public Object getEntity() {
		return entity;
	}

}
