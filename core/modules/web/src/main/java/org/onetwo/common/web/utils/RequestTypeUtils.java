package org.onetwo.common.web.utils;

import javax.servlet.http.HttpServletRequest;

abstract public class RequestTypeUtils {

	public static class AjaxKeys {
		public static final String MESSAGE_KEY = "message";
		public static final String MESSAGE_CODE_KEY = "message_code";
//		public static final Integer RESULT_SUCCEED = 1;
//		public static final Integer RESULT_FAILED = 0;
	}
	
	public static final String HEADER_KEY = "X-Requested-With";

	public static enum RequestType {
		Http("HttpReqeust"),
		Flash("FlashRequest"),
		Ajax("XMLHttpRequest"),
		Unknow("UnknowRequest"),;
		
		private String key;
		
		RequestType(String key){
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}

		public static RequestType valueOfKey(String key){
			RequestType[] reqTypes = RequestType.values();
			for(RequestType rt : reqTypes){
				if(rt.getKey().equals(key))
					return rt;
			}
			return RequestType.Unknow;
		}
	}
	
	public static RequestType getRequestType(String key){
		return RequestType.valueOfKey(key);
	}
	
	public static RequestType getRequestType(HttpServletRequest request){
		String reqeustKey = request.getHeader(RequestTypeUtils.HEADER_KEY);
		RequestType requestType = RequestTypeUtils.getRequestType(reqeustKey);
		return requestType;
	}
	
	public static void main(String[] args){
		String key = "XMLHttpRequest";
		RequestType req = RequestType.valueOfKey(key);
		System.out.println(req);
	}
}
