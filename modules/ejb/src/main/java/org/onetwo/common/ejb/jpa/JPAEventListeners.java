package org.onetwo.common.ejb.jpa;

import org.onetwo.common.db.event.DbEventListeners;
import org.onetwo.common.db.event.RemoveEventListener;
import org.onetwo.common.db.event.SaveOrUpdateEventListener;

public class JPAEventListeners extends DbEventListeners {

	@Override
	public void init() {
		this.removeEventListeners = new RemoveEventListener[]{new JPARemoveEventListener()};
		this.saveOrUpdateEventListeners = new SaveOrUpdateEventListener[]{new JPASaveOrUpdateEventListener()};
	}

}
