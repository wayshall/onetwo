package org.onetwo.plugins.zkclient;

import java.util.Optional;

import org.onetwo.common.spring.plugin.AbstractLoadingConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

public class ZkclientPluginConfig extends AbstractLoadingConfig {
	
	private Optional<ZkclientType> clientType;
	private String servers;
	private int sessionTimeout;
	private String rootNode;
	private String serverNode;
	
	@Override
	protected void initConfig(JFishProperties config) {
		clientType = Optional.ofNullable(config.getEnum("client.type", ZkclientType.class));
//		Assert.notNull(clientType);
		
		
		servers = config.getProperty("servers", "127.0.0.1:2181");
		sessionTimeout = config.getInt("session.timeout", 5000);

		this.rootNode =  config.getPath("rootNode", "/jfish");
		
		
		logger.info("=========== zkclient config start ===========");
		logger.info("serverNode: {}", serverNode);
		logger.info("=========== zkclient config end ===========");
	}
	
	
	public String getRootNode() {
		return rootNode;
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


	public static enum ZkclientType {
		PROVIDER,
		CONSUME
	}

}
