package org.onetwo.common.jsonrpc.utils;

public class ZkUtils {
	
	final public static class ConfigValue {
		private ConfigValue(){}
		public static final String ZK = "zk";
		public static final String PROVIDER_PATH = "/provider";
		public static final String CONSUMER_PATH = "/consumer";
	}
	
	private ZkUtils(){}

}
