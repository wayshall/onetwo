package org.onetwo.plugins.session;

import org.onetwo.common.spring.plugin.ConfigurableContextPlugin.LoadableConfig;
import org.onetwo.common.utils.propconf.JFishProperties;

public class SessionPluginConfig implements LoadableConfig {
	
	public static enum SessionRepository {
		CONTAINER,
		REDIS
	}

	private JFishProperties config;
	private SessionRepository sessionRepository;

	public SessionPluginConfig() {
	}
	
	@Override
	public void load(JFishProperties properties) {
		this.config = properties;
		String sr = properties.getProperty("session.repository", SessionRepository.CONTAINER.name());
		this.sessionRepository = SessionRepository.valueOf(sr.toUpperCase());
	}
	
	public boolean isContainerSession(){
		return getSessionRepository()==SessionRepository.CONTAINER;
	}


	public SessionRepository getSessionRepository() {
		return sessionRepository;
	}

	@Override
	public JFishProperties getSourceConfig() {
		return config;
	}

}
