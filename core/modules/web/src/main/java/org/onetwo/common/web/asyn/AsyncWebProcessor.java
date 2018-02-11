package org.onetwo.common.web.asyn;


public interface AsyncWebProcessor {

	public void handleTask(AsyncTask task);
	
	public AsyncMessageHolder getAsynMessageHolder();

}