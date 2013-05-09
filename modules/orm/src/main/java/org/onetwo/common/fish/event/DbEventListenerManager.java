package org.onetwo.common.fish.event;


public interface DbEventListenerManager {

	public JFishEventListener[] getJFishEventListener(JFishEventAction eventAction);
	
	public DbEventListenerManager registerDefaultEventListeners();

	/*public InsertEventListener[] getInsertEventListeners();

	public void setInsertEventListeners(InsertEventListener... insertEventListeners);

	public InsertEventListener[] getBatchInsertEventListeners();

	public void setBatchInsertEventListeners(InsertEventListener... batchInsertEventListeners);

	public UpdateEventListener[] getUpdateEventListeners();

	public void setUpdateEventListeners(UpdateEventListener... updateEventListeners);

	public JFishDeleteEventListener[] getDeleteEventListeners();

	public void setDeleteEventListeners(JFishDeleteEventListener... deleteEventListeners);

	public JFishFindEventListener[] getFindEventListeners();

	public void setFindEventListeners(JFishFindEventListener... findEventListeners);*/

}