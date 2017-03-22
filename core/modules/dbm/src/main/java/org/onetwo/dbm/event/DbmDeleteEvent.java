package org.onetwo.dbm.event;

public class DbmDeleteEvent extends DbmSessionEvent{
	
	public static enum DeleteType {
		byEntity,
		byIdentify,
		deleteAll,
	}

	public DbmDeleteEvent(Object object, DbmSessionEventSource eventSource) {
		super(object, DbmEventAction.delete, eventSource);
	}

	private DeleteType deleteType = DeleteType.byEntity;//DeleteType.byIdentify;//

	public boolean isDeleteByIdentify() {
		return DeleteType.byIdentify==deleteType;
	}

	public boolean isDeleteAll() {
		return DeleteType.deleteAll==deleteType;
	}

	public DeleteType getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(DeleteType deleteType) {
		this.deleteType = deleteType;
	}
	
}
