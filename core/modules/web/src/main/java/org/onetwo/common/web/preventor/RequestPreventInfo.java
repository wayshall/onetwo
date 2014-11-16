package org.onetwo.common.web.preventor;

public class RequestPreventInfo {
	
	private final boolean repeateSubmitValidate;
	private final boolean csrfValidate;
	public RequestPreventInfo(boolean repeateSubmit, boolean csrfValidate) {
		super();
		this.repeateSubmitValidate = repeateSubmit;
		this.csrfValidate = csrfValidate;
	}
	public boolean isRepeateSubmitValidate() {
		return repeateSubmitValidate;
	}
	public boolean isCsrfValidate() {
		return csrfValidate;
	}
	
	public String getKey(){
		return repeateSubmitValidate+"-"+csrfValidate;
	}
	
	@Override
	public String toString() {
		return "RequestPreventInfo [repeateSubmitValidate=" + repeateSubmitValidate + ", csrfValidate=" + csrfValidate + "]";
	}
	
	

}
