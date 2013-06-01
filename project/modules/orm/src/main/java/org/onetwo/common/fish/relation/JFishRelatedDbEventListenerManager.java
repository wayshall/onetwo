	package org.onetwo.common.fish.relation;

import org.onetwo.common.fish.event.DbEventListenerManager;
import org.onetwo.common.fish.event.InsertEventListener;
import org.onetwo.common.fish.event.JFishDefaultDbEventListenerManager;
import org.onetwo.common.fish.event.JFishDeleteEventListener;
import org.onetwo.common.fish.event.JFishEventAction;
import org.onetwo.common.fish.event.JFishEventListener;
import org.onetwo.common.fish.event.JFishFindEventListener;
import org.onetwo.common.fish.event.JFishInsertOrUpdateListener;
import org.onetwo.common.fish.event.UpdateEventListener;

public class JFishRelatedDbEventListenerManager implements DbEventListenerManager  {
	
	private JFishDefaultDbEventListenerManager dbEventListenerManager;
	

	public JFishRelatedDbEventListenerManager(JFishDefaultDbEventListenerManager dbEventListenerManager) {
		super();
		this.dbEventListenerManager = dbEventListenerManager;
	}

	@Override
	public DbEventListenerManager registerDefaultEventListeners(){
		this.dbEventListenerManager.registerDefaultEventListeners();
		
		InsertEventListener[] insertListeners = new InsertEventListener[dbEventListenerManager.getInsertEventListeners().length];
		int index = 0;
		for(InsertEventListener l : dbEventListenerManager.getInsertEventListeners()){
			insertListeners[index++] = new JFishRelatedInsertEventListener(l);
		}
		this.setInsertEventListeners(insertListeners);
		
		UpdateEventListener[] updates = new UpdateEventListener[dbEventListenerManager.getUpdateEventListeners().length];
		index = 0;
		for(UpdateEventListener l : dbEventListenerManager.getUpdateEventListeners()){
			updates[index++] = new JFishRelatedUpdateEventListener(l);
		}
		this.setUpdateEventListeners(updates);
		
		JFishDeleteEventListener[] deletes = new JFishDeleteEventListener[dbEventListenerManager.getDeleteEventListeners().length];
		index = 0;
		for(JFishDeleteEventListener l : dbEventListenerManager.getDeleteEventListeners()){
			deletes[index++] = new JFishRelatedDeleteEventListener(l);
		}
		this.setDeleteEventListeners(deletes);
		
		JFishInsertOrUpdateListener[] insertOrUpdates = new JFishInsertOrUpdateListener[dbEventListenerManager.getInsertOrUpdateEventListeners().length];
		index = 0;
		for(JFishInsertOrUpdateListener l : dbEventListenerManager.getInsertOrUpdateEventListeners()){
			insertOrUpdates[index++] = new JFishRelatedInsertOrUpdateEventListener(l);
		}
		this.setInsertOrUpdateEventListeners(insertOrUpdates);
		
//		this.setBatchInsertEventListeners(dbEventListenerManager.getBatchInsertEventListeners());
//		this.setFindEventListeners(dbEventListenerManager.getFindEventListeners());
		return this;
	}

	public JFishEventListener[] getJFishEventListener(JFishEventAction eventAction) {
		return dbEventListenerManager.getJFishEventListener(eventAction);
	}

	public InsertEventListener[] getInsertEventListeners() {
		return dbEventListenerManager.getInsertEventListeners();
	}

	public void setInsertEventListeners(InsertEventListener... insertEventListeners) {
		dbEventListenerManager.setInsertEventListeners(insertEventListeners);
	}

	public InsertEventListener[] getBatchInsertEventListeners() {
		return dbEventListenerManager.getBatchInsertEventListeners();
	}

	public void setBatchInsertEventListeners(InsertEventListener... batchInsertEventListeners) {
		dbEventListenerManager.setBatchInsertEventListeners(batchInsertEventListeners);
	}

	public UpdateEventListener[] getUpdateEventListeners() {
		return dbEventListenerManager.getUpdateEventListeners();
	}

	public void setUpdateEventListeners(UpdateEventListener... updateEventListeners) {
		dbEventListenerManager.setUpdateEventListeners(updateEventListeners);
	}

	public JFishDeleteEventListener[] getDeleteEventListeners() {
		return dbEventListenerManager.getDeleteEventListeners();
	}

	public void setDeleteEventListeners(JFishDeleteEventListener... deleteEventListeners) {
		dbEventListenerManager.setDeleteEventListeners(deleteEventListeners);
	}

	public JFishFindEventListener[] getFindEventListeners() {
		return dbEventListenerManager.getFindEventListeners();
	}

	public void setFindEventListeners(JFishFindEventListener... findEventListeners) {
		dbEventListenerManager.setFindEventListeners(findEventListeners);
	}

	public JFishEventListener[] getSaveRefEventListeners() {
		return dbEventListenerManager.getSaveRefEventListeners();
	}

	public void setSaveRefEventListeners(JFishEventListener[] saveRefEventListeners) {
		dbEventListenerManager.setSaveRefEventListeners(saveRefEventListeners);
	}

	public JFishEventListener[] getDropRefEventListeners() {
		return dbEventListenerManager.getDropRefEventListeners();
	}

	public void setDropRefEventListeners(JFishEventListener[] dropRefEventListeners) {
		dbEventListenerManager.setDropRefEventListeners(dropRefEventListeners);
	}

	public JFishInsertOrUpdateListener[] getInsertOrUpdateEventListeners() {
		return dbEventListenerManager.getInsertOrUpdateEventListeners();
	}

	public void setInsertOrUpdateEventListeners(JFishInsertOrUpdateListener[] insertOrUpdateEventListeners) {
		dbEventListenerManager.setInsertOrUpdateEventListeners(insertOrUpdateEventListeners);
	}

	
}
	