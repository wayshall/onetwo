package org.onetwo.common.db.event;


public class DbEventListeners {
//	private AnnotationProcessorManager annotationProcessorManager;
	
	protected RemoveEventListener[] removeEventListeners;
	protected SaveOrUpdateEventListener[] saveOrUpdateEventListeners;
	
	public DbEventListeners(){
//		this.annotationProcessorManager = new DefaultAnnotationProcessorManager();
		this.init();
	}
	
	public void init(){
	}

	/*public AnnotationProcessorManager getAnnotationProcessorManager() {
		return annotationProcessorManager;
	}*/

	public RemoveEventListener[] getRemoveEventListeners() {
		return removeEventListeners;
	}

	public void setRemoveEventListeners(RemoveEventListener[] removeEventListeners) {
		this.removeEventListeners = removeEventListeners;
	}

	public SaveOrUpdateEventListener[] getSaveOrUpdateEventListeners() {
		return saveOrUpdateEventListeners;
	}

	public void setSaveOrUpdateEventListeners(SaveOrUpdateEventListener[] saveOrUpdateEventListeners) {
		this.saveOrUpdateEventListeners = saveOrUpdateEventListeners;
	}


}
