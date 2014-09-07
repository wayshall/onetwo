package org.onetwo.common.web.asyn;

import org.onetwo.common.web.asyn2.AsyncTask;

public interface AsyncWebProcessor<MSG> {

	public void handleTask(AsyncTask task);
	
	public AsyncMessageTunnel<MSG> getAsynMessageTunnel();

}