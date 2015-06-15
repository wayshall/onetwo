package org.onetwo.common.jsonrpc.zk;

public class ServerPathData {
	
	private String serverUrl;
	private int clientCount;
	
	
	public ServerPathData() {
		super();
	}
	public ServerPathData(String serverUrl) {
		super();
		this.serverUrl = serverUrl;
	}
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public int getClientCount() {
		return clientCount;
	}
	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	public void increase(int count){
		this.clientCount += count;
	}
	
}
