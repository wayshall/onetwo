package org.onetwo.common.jsonrpc.plugin;

import org.onetwo.common.jsonrpc.utils.RpcUtils.ServerEndpointConstant;
import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.NetUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;

import com.google.common.base.CharMatcher;

public class AbstractRpcPluginConfig extends AbstractLoadingConfig {
	
	public static final String RPC_PROVIDER = "rpc.provider";
	
	protected RpcProvider rpcProvider;
	
	@Override
	protected void initConfig(JFishProperties config) {
//		this.rpcProvider = config.getOptionalEnum(RPC_PROVIDER, RpcProvider.class).orElse(RpcProvider.DIRECT);
		String provider = config.getProperty(RPC_PROVIDER, RpcProvider.DIRECT.toString());
		this.rpcProvider = RpcProvider.valueOf(provider.toUpperCase());
	}

	public RpcProvider getRpcProvider() {
		return rpcProvider;
	}

	public static enum RpcProvider {
		DIRECT,
		ZK
	}
	
	final protected String parseAddress(String address){
//		String realAddress = StringUtils.appendEndWith(address, "/");
		String realAddress = address;
		if(address.startsWith(":")){
			//只写端口
			realAddress = NetUtils.getLocalAddress() + address;
		}
//		return encodePath(realAddress);
		realAddress = getHttpAddress(realAddress);
		return realAddress;
	}
	

	final protected String getHttpAddress(String address){
		String acutalPath = address;
		if(!acutalPath.startsWith(ServerEndpointConstant.HTTP_KEY) && !acutalPath.startsWith(ServerEndpointConstant.HTTPS_KEY)){
			acutalPath = ServerEndpointConstant.HTTP_KEY + acutalPath;
		}
		acutalPath = StringUtils.appendEndWith(acutalPath, "/");;
		return acutalPath;
	}

	static public String decodePath(String path){
		return CharMatcher.anyOf("|").replaceFrom(path, '/');
	}
	
	static public String encodePath(String path){
		return CharMatcher.anyOf("/").replaceFrom(path, '|');
	}
}
