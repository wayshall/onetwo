package org.onetwo.plugins.zkclient.utils;

import com.google.common.base.CharMatcher;


public class ZkUtils {
	

	static public String decodePath(String path){
		return CharMatcher.anyOf("|").replaceFrom(path, '/');
	}
	
	static public String encodePath(String path){
		return CharMatcher.anyOf("/").replaceFrom(path, '|');
	}
	
	/*public String create(final String path, byte data[], List<ACL> acl, CreateMode createMode){
		
	}*/
	private ZkUtils(){
	}

}
