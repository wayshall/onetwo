package org.onetwo.plugins.jsonrpc;

import java.util.Optional;

import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.NetUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.common.web.config.BaseSiteConfig;

public class JsonRpcPluginConfig extends AbstractLoadingConfig {
	
	private Optional<ZkclientType> clientType;
	private String servers;
	private int sessionTimeout;
	private String baseNode = "/jfish";
	private String moduleNode;
	private String serverNode;
	private String serviceNode;
	private String clientNode;
	
	@Override
	protected void initConfig(JFishProperties config) {
		clientType = Optional.ofNullable(config.getEnum("client.type", ZkclientType.class));
//		Assert.notNull(clientType);
		
		
		servers = config.getProperty("servers", "127.0.0.1:2181");
		sessionTimeout = config.getInt("session.timeout", 5000);
		
		this.moduleNode = baseNode + "/" + config.getPath("moduleNode", BaseSiteConfig.getInstance().getAppCode());
		this.serverNode = moduleNode + "/" + config.getPath("serverNode", NetUtils.getLocalAddress());
		this.serviceNode = serverNode + "/" + config.getPath("serviceNode", "service");
		this.clientNode = serverNode + "/" + config.getPath("clientNode", "client");
		
		logger.info("=========== zkclient config start ===========");
		logger.info("moduleNode: {}", moduleNode);
		logger.info("serverNode: {}", serverNode);
		logger.info("serviceNode: {}", serviceNode);
		logger.info("clientNode: {}", clientNode);
		logger.info("=========== zkclient config end ===========");
	}
	
	
	public ZkclientType getClientType() {
		return clientType.orElse(ZkclientType.PROVIDER);
	}

	public String getServers() {
		return servers;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public String getBaseNode() {
		return baseNode;
	}

	public String getModuleNode() {
		return moduleNode;
	}

	public String getServerNode() {
		return serverNode;
	}

	public String getServiceNode() {
		return serviceNode;
	}

	public String getClientNode() {
		return clientNode;
	}

	public static enum ZkclientType {
		PROVIDER,
		CONSUME
	}

}
