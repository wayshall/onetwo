package org.onetwo.cloud.webmgr;

import java.util.Map;

import org.onetwo.boot.webmgr.WebManagementCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.endpoint.RefreshEndpoint;

/**
 * @author weishao zeng
 * <br/>
 */
public class RefreshEndpointWebCommand implements WebManagementCommand {
	@Autowired
	private RefreshEndpoint refreshEndpoint;

	
	@Override
	public String getName() {
		return "refreshConfig";
	}


	@Override
	public Object invoke(Map<String, Object> data) {
		Object result = refreshEndpoint.refresh();
		return result;
	}
	
}
