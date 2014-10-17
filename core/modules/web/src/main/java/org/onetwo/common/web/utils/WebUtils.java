package org.onetwo.common.web.utils;


final public class WebUtils {

//	public static final String DEFAULT_TOKEN_FIELD_NAME = "org.onetwo.jfish.form.token";

	public static final String FORWARD_KEY = "forward:";
	
	private WebUtils(){}
	

	public static boolean hasForwardPrefix(String path){
		return path.startsWith(FORWARD_KEY);
	}
	
	public static String getNoForward(String path){
		if(path.startsWith(FORWARD_KEY)){
			return path.substring(FORWARD_KEY.length());
		}
		return path;
	}

	public static String forwardPrefix(String path){
		if(!path.startsWith(FORWARD_KEY)){
			return FORWARD_KEY + path;
		}
		return path;
	}
	
}
