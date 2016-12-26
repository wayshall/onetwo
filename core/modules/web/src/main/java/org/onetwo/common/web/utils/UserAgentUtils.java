package org.onetwo.common.web.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

public abstract class UserAgentUtils {

	public static UserAgent getUserAgent(){
		Optional<HttpServletRequest> reqOpt = WebHolder.getSpringContextHolderRequest();
		if(!reqOpt.isPresent()){
			return null;
		}
		return getUserAgent(WebHolder.getSpringContextHolderRequest().get());
	}

	public static UserAgent getUserAgent(HttpServletRequest request){
		UserAgent userAgent = UserAgent.parseUserAgentString(RequestUtils.getUserAgent(request));
		return userAgent;
	}

	public static boolean isMobileRequest(){
		return DeviceType.MOBILE.equals(getRequestDeviceType());
	}
	public static boolean isMobileRequest(HttpServletRequest request){
		return DeviceType.MOBILE.equals(getRequestDeviceType(request));
	}
	
	public static DeviceType getRequestDeviceType(){
		UserAgent userAgent = getUserAgent();
		return userAgent.getOperatingSystem().getDeviceType();
	}
	
	public static DeviceType getRequestDeviceType(HttpServletRequest request){
		UserAgent userAgent = getUserAgent(request);
		return userAgent.getOperatingSystem().getDeviceType();
	}
	
}
