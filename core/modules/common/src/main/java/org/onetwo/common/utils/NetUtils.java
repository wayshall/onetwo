package org.onetwo.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtils {
	public static final String LOCAL_IP = "127.0.0.1";

	public static String getLocalAddress(){
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return LOCAL_IP;
		}
	}
	private NetUtils(){
	}

}
