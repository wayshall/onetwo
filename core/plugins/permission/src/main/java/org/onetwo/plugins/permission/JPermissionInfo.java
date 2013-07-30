package org.onetwo.plugins.permission;

import java.util.Collection;


public class JPermissionInfo extends JResourceInfo {

	public JPermissionInfo(String id, String label) {
		super(id, label);
	}
	private Collection<RequestInfo> requestInfos;
	
	@Override
	public boolean isPermission() {
		return true;
	}
	public Collection<RequestInfo> getRequestInfos() {
		return requestInfos;
	}
	public void setRequestInfos(Collection<RequestInfo> requestInfos) {
		this.requestInfos = requestInfos;
	}
}
