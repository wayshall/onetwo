package org.onetwo.cloud.webmgr;

import org.onetwo.boot.webmgr.WebManagementDelegater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.endpoint.RefreshEndpoint;

/**
 * @author weishao zeng
 * <br/>
 */
public class CloudManagementDelegater implements WebManagementDelegater {
	@Autowired
	private RefreshEndpoint refreshEndpoint;

	@Override
	public Object invoke(String command) {
		Object result = null;
		if ("refreshConfig".equals(command)) {
			result = refreshEndpoint.invoke();
		} else {
			result = command + " not found!";
		}
		return result;
	}
	
}
