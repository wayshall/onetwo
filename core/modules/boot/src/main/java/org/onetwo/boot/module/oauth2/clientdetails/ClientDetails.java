package org.onetwo.boot.module.oauth2.clientdetails;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
public class ClientDetails implements Serializable {
	private String clientId;
	private String clientName;
	
	private Map<String, Object> properties;
	
	public ClientDetails(String clientId, String clientName) {
		super();
		this.clientId = clientId;
		this.clientName = clientName;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProperty(String key, T def){
		T val = (T)getProperties().get(key);
		if(val==null){
			return def;
		}
		return val;
	}
	
	public Map<String, Object> getProperties() {
		if(properties==null){
			return Collections.emptyMap();
		}
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
