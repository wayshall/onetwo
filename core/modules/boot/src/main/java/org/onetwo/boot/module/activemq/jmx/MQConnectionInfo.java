package org.onetwo.boot.module.activemq.jmx;

import org.apache.activemq.broker.jmx.ConnectionViewMBean;
import org.onetwo.common.exception.BaseException;

/**
 * @author weishao zeng
 * <br/>
 */
public class MQConnectionInfo {
	final private transient ConnectionViewMBean viewMBean;
	final private String name;
	final private String clientId;

	public MQConnectionInfo(String connName, ConnectionViewMBean viewMBean) {
		super();
		this.viewMBean = viewMBean;
		this.name = connName;
		this.clientId = viewMBean.getClientId();
	}
	
	public void stop() {
		try {
			this.viewMBean.stop();
		} catch (Exception e) {
			throw new BaseException("stop client[" + clientId + "] error : " + e.getMessage(), e);
		}
	}

	public String getName() {
		return name;
	}

	public String getClientId() {
		return clientId;
	}
	
	public String toString() {
		return "connection: " + name + ", clientId: " + clientId;
	}

}
