package org.onetwo.common.ejb.jms;

import java.util.Properties;

import javax.jms.Session;

public class MessageConf {
	
	private Properties contextProps;
	private String connectionFactoryName;
	
	private String destination;
	
	private boolean transacted;
	private int sessionMode = Session.AUTO_ACKNOWLEDGE;
	
	private boolean lazyInit;

	public Properties getContextProps() {
		return contextProps;
	}

	public void setContextProps(Properties contextProps) {
		this.contextProps = contextProps;
	}

	public String getConnectionFactoryName() {
		return connectionFactoryName;
	}

	public void setConnectionFactoryName(String connectionFactoryName) {
		this.connectionFactoryName = connectionFactoryName;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public boolean isTransacted() {
		return transacted;
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}

	public int getSessionMode() {
		return sessionMode;
	}

	public void setSessionMode(int sessionMode) {
		this.sessionMode = sessionMode;
	}

	public boolean isLazyInit() {
		return lazyInit;
	}

	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}
	
}
