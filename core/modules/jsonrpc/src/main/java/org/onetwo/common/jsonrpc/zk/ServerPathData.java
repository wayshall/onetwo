package org.onetwo.common.jsonrpc.zk;

public class ServerPathData {
	
	private String url;
	private int clientCount;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getClientCount() {
		return clientCount;
	}
	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	
}
