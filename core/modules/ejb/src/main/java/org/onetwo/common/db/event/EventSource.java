package org.onetwo.common.db.event;

import org.onetwo.common.db.BaseEntityManager;

public interface EventSource extends BaseEntityManager {
	abstract public <T> T getEntityManager();
}
