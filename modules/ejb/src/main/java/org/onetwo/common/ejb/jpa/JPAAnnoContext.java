package org.onetwo.common.ejb.jpa;

import org.onetwo.common.db.event.DefaultEvent;
import org.onetwo.common.utils.annotation.AnnoContext;

public class JPAAnnoContext extends AnnoContext {
	
	private DefaultEvent event;
	
	public JPAAnnoContext(Object srcObject, Enum eventAction) {
		super(srcObject, eventAction);
	}

	public DefaultEvent getEvent() {
		return event;
	}

	public void setEvent(DefaultEvent event) {
		this.event = event;
	}

}
