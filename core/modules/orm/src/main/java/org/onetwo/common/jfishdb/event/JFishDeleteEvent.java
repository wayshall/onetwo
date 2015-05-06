package org.onetwo.common.jfishdb.event;

public class JFishDeleteEvent extends JFishEvent{
	
	public static enum DeleteType {
		byEntity,
		byIdentify,
		deleteAll,
	}

	public JFishDeleteEvent(Object object, JFishEventSource eventSource) {
		super(object, JFishEventAction.delete, eventSource);
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
