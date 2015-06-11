package org.onetwo.common.jsonrpc.utils;

public class RpcUtils {
	
	final public static class ConfigValue {
		private ConfigValue(){}
		public static final String ZK = "zk";
		public static final String PROVIDER_PATH = "/provider";
		public static final String CONSUMER_PATH = "/consumer";
	}
	
	final public static class ServerEndpointConstant {
		private ServerEndpointConstant(){}
		public static final String HTTP_KEY = "http://";
		public static final String HTTPS_KEY = "https://";
		public static final String JSONRPC_POSTFIX_PATH = "/jsonrpc/";
	}
	
	private RpcUtils(){}

}
