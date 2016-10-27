package org.onetwo.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

public class NetUtils {
	public static final String LOCAL_IP = "127.0.0.1";

	public static String getHostAddress(){
		return getInetAddress().map(i->i.getHostAddress()).orElse(LOCAL_IP);
	}
	public static String getHostName(){
		return getInetAddress().map(i->i.getHostName()).orElse(LOCAL_IP);
	}

	public static Optional<InetAddress> getInetAddress(){
		try {
			return Optional.ofNullable(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			return Optional.empty();
		}
	}
	private NetUtils(){
	}

}
