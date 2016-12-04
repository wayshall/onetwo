package org.onetwo.common.web.asyn;


public interface AsyncWebProcessor<MSG> {

	public void handleTask(AsyncTask task);
	
	public AsyncMessageTunnel<MSG> getAsynMessageTunnel();

}