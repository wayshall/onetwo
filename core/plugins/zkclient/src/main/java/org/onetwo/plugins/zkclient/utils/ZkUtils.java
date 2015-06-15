package org.onetwo.plugins.zkclient.utils;

import com.google.common.base.CharMatcher;


public class ZkUtils {

	public static final byte[] EMPTY_ARRAY = new byte[0];
	

	public static String decodePath(String path){
		return CharMatcher.anyOf("|").replaceFrom(path, '/');
	}
	
	public static String encodePath(String path){
		return CharMatcher.anyOf("/").replaceFrom(path, '|');
	}
	

	public static boolean isEmpty(byte[] data) {
		return (data == null || data.length == 0);
	}

	
	/*public String create(final String path, byte data[], List<ACL> acl, CreateMode createMode){
		
	}*/
	private ZkUtils(){
	}

}
