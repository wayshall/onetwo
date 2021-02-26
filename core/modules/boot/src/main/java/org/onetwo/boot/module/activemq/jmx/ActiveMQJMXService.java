package org.onetwo.boot.module.activemq.jmx;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.Map;

import org.apache.activemq.broker.jmx.ConnectionViewMBean;
import org.apache.activemq.web.RemoteJMXBrokerFacade;
import org.apache.activemq.web.config.SystemPropertiesConfiguration;
import org.onetwo.common.exception.BaseException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.beust.jcommander.internal.Maps;

public class ActiveMQJMXService implements InitializingBean {
	
	@Autowired
	private ActiveMQJmxServiceProperties jmxProps;
	private RemoteJMXBrokerFacade jmxBrokerFacade;

	@Override
	public void afterPropertiesSet() throws Exception {
		jmxProps.check();
		
//        JMXServiceURL url = new JMXServiceURL(jmxProps.getServiceUrl());
//        Map<String, String[]> environment = Maps.newHashMap();
//        environment.put(JMXConnector.CREDENTIALS, new String[]{jmxProps.getUsername(), jmxProps.getPassword()});
//        JMXConnector jmxConnector = JMXConnectorFactory.connect(url, environment);
		
        System.setProperty(SystemPropertiesConfiguration.PROPERTY_JMX_URL, jmxProps.getServiceUrl());
        System.setProperty(SystemPropertiesConfiguration.PROPERTY_JMX_USER, jmxProps.getUsername());
        System.setProperty(SystemPropertiesConfiguration.PROPERTY_JMX_PASSWORD, jmxProps.getPassword());
        SystemPropertiesConfiguration configuration = new SystemPropertiesConfiguration();
		RemoteJMXBrokerFacade brokerFacade = new RemoteJMXBrokerFacade();
        brokerFacade.setConfiguration(configuration);
        
        this.jmxBrokerFacade = brokerFacade;
	}
	
	/***
	 * 获取所有mqtt连接
	 * @author weishao zeng
	 * @return
	 */
	public Map<String, MQConnectionInfo> getMqttConnections() {
		return getConnections("mqtt");
	}
	
	/***
	 * 
	 * @author weishao zeng
	 * @param connectorName example: mqtt
	 * @return clientId -> connection
	 */
	public Map<String, MQConnectionInfo> getConnections(String connectorName) {
		Map<String, MQConnectionInfo> conMap = Maps.newHashMap();
		try {
			for (String conName : this.jmxBrokerFacade.getConnections(connectorName)) {
				ConnectionViewMBean conView = this.jmxBrokerFacade.getConnection(conName);
				MQConnectionInfo conInfo = new MQConnectionInfo(conName, conView);
				conMap.put(conInfo.getClientId(), conInfo);
			}
		} catch (Exception e) {
			throw new BaseException("get [" + connectorName + "] client connection error: " + e.getMessage(), e);
		}
		return conMap;
	}
	

}
