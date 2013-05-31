	package org.onetwo.common.fish.event;

import org.onetwo.common.fish.exception.JFishDbException;

public class JFishDefaultDbEventListenerManager implements DbEventListenerManager {

	protected JFishInsertOrUpdateListener[] insertOrUpdateEventListeners;
	protected InsertEventListener[] insertEventListeners;
	protected InsertEventListener[] batchInsertEventListeners;
	protected UpdateEventListener[] batchUpdateEventListeners;
	protected UpdateEventListener[] updateEventListeners;
	protected JFishDeleteEventListener[] deleteEventListeners;
	protected JFishFindEventListener[] findEventListeners;

	protected JFishEventListener[] saveRefEventListeners;
	protected JFishEventListener[] dropRefEventListeners;
	
	@Override
	public DbEventListenerManager registerDefaultEventListeners(){
		if(this.insertOrUpdateEventListeners==null){
			this.insertOrUpdateEventListeners = new JFishInsertOrUpdateListener[]{new JFishInsertOrUpdateListener()};
		}
		if(this.insertEventListeners==null){
			this.insertEventListeners = new InsertEventListener[]{getDefaultInsertEventListener()};
		}
		if(this.batchInsertEventListeners==null){
			this.batchInsertEventListeners = new InsertEventListener[]{getDefaultBatchInsertEventListener()};
		}
		if(this.updateEventListeners==null){
			this.updateEventListeners = new UpdateEventListener[]{getDefaultUpdateEventListener()};
		}
		if(this.batchUpdateEventListeners==null){
			this.batchUpdateEventListeners = new UpdateEventListener[]{getDefaultBatchUpdateEventListener()};
		}
		if(this.deleteEventListeners==null){
			this.deleteEventListeners = new JFishDeleteEventListener[]{getDefaultDeleteEventListener()};
		}
		if(this.findEventListeners==null){
			this.findEventListeners = new JFishFindEventListener[]{getDefaultFindEventListener()};
		}
		
		if(this.saveRefEventListeners==null){
			this.saveRefEventListeners = new JFishSaveRefListener[]{new JFishSaveRefListener()};
		}
		if(this.dropRefEventListeners==null){
			this.dropRefEventListeners = new JFishDropRefListener[]{new JFishDropRefListener()};
		}
		/*if(this.queryableEventListeners==null){
			this.queryableEventListeners = new JFishQueryableEventListener[]{new JFishQueryableEventListener()};
		}*/
		
		return this;
	}
	
	protected InsertEventListener getDefaultInsertEventListener(){
		return new JFishInsertEventListener();
	}
	
	protected InsertEventListener getDefaultBatchInsertEventListener(){
		return new JFishBatchInsertEventListener();
	}
	
	protected UpdateEventListener getDefaultUpdateEventListener(){
		return new JFishUpdateEventListener();
	}
	
	protected UpdateEventListener getDefaultBatchUpdateEventListener(){
		return new JFishBatchUpdateEventListener();
	}
	
	protected JFishDeleteEventListener getDefaultDeleteEventListener(){
		return new JFishDeleteEventListener();
	}
	
	protected JFishFindEventListener getDefaultFindEventListener(){
		return new JFishFindEventListener();
	}
	
	public JFishEventListener[] getJFishEventListener(JFishEventAction eventAction){
		JFishEventListener[] listeners = null;
		if(eventAction==JFishEventAction.insert){
			listeners = getInsertEventListeners();
		}else if(eventAction==JFishEventAction.update){
			listeners = getUpdateEventListeners();
		}else if(eventAction==JFishEventAction.insertOrUpdate){
			listeners = getInsertOrUpdateEventListeners();
		}else if(eventAction==JFishEventAction.batchInsert){
			listeners = getBatchInsertEventListeners();
		}else if(eventAction==JFishEventAction.batchUpdate){
			listeners = getBatchUpdateEventListeners();
		}else if(eventAction==JFishEventAction.delete){
			listeners = getDeleteEventListeners();
		}else if(eventAction==JFishEventAction.find){
			listeners = getFindEventListeners();
		}else if(eventAction==JFishEventAction.saveRef){
			listeners = getSaveRefEventListeners();
		}else if(eventAction==JFishEventAction.dropRef){
			listeners = getDropRefEventListeners();
		}else if(eventAction==JFishEventAction.queryable){
			//TODO
		}else{
			throw new JFishDbException("don't supported this event action: " + eventAction);
		}
		return listeners;
	}
	
	public InsertEventListener[] getInsertEventListeners() {
		return insertEventListeners;
	}

	public void setInsertEventListeners(InsertEventListener... insertEventListeners) {
		this.insertEventListeners = insertEventListeners;
	}

	public InsertEventListener[] getBatchInsertEventListeners() {
		return batchInsertEventListeners;
	}

	public void setBatchInsertEventListeners(InsertEventListener... batchInsertEventListeners) {
		this.batchInsertEventListeners = batchInsertEventListeners;
	}

	public UpdateEventListener[] getUpdateEventListeners() {
		return updateEventListeners;
	}

	public void setUpdateEventListeners(UpdateEventListener... updateEventListeners) {
		this.updateEventListeners = updateEventListeners;
	}

	public JFishDeleteEventListener[] getDeleteEventListeners() {
		return deleteEventListeners;
	}

	public void setDeleteEventListeners(JFishDeleteEventListener... deleteEventListeners) {
		this.deleteEventListeners = deleteEventListeners;
	}

	public JFishFindEventListener[] getFindEventListeners() {
		return findEventListeners;
	}

	public void setFindEventListeners(JFishFindEventListener... findEventListeners) {
		this.findEventListeners = findEventListeners;
	}

	public JFishEventListener[] getSaveRefEventListeners() {
		return saveRefEventListeners;
	}

	public void setSaveRefEventListeners(JFishEventListener[] saveRefEventListeners) {
		this.saveRefEventListeners = saveRefEventListeners;
	}

	public JFishEventListener[] getDropRefEventListeners() {
		return dropRefEventListeners;
	}

	public void setDropRefEventListeners(JFishEventListener[] dropRefEventListeners) {
		this.dropRefEventListeners = dropRefEventListeners;
	}

	public JFishInsertOrUpdateListener[] getInsertOrUpdateEventListeners() {
		return insertOrUpdateEventListeners;
	}

	public void setInsertOrUpdateEventListeners(JFishInsertOrUpdateListener[] insertOrUpdateEventListeners) {
		this.insertOrUpdateEventListeners = insertOrUpdateEventListeners;
	}

	public UpdateEventListener[] getBatchUpdateEventListeners() {
		return batchUpdateEventListeners;
	}

	public void setBatchUpdateEventListeners(UpdateEventListener[] batchUpdateEventListeners) {
		this.batchUpdateEventListeners = batchUpdateEventListeners;
	}
	
}
	