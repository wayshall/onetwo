package org.onetwo.common.jfishdbm.exception;



@SuppressWarnings("serial")
public class UpdateCountException extends DbmException{

	final private int expectCount;
	final private int effectiveCount;

	public UpdateCountException(String msg, int expectCount, int effectiveCount) {
		super(msg + " expect effective: " + expectCount+", but actual effective: " + effectiveCount);
		this.expectCount = expectCount;
		this.effectiveCount = effectiveCount;
	}

	public int getExpectCount() {
		return expectCount;
	}

	public int getEffectiveCount() {
		return effectiveCount;
	}

	protected String getDefaultCode(){
		return JFishErrorCode.ORM_ERROR;
	}

}
