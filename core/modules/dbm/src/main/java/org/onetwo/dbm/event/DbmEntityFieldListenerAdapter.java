package org.onetwo.dbm.event;

import org.onetwo.common.utils.JFishProperty;

public class DbmEntityFieldListenerAdapter implements DbmEntityFieldListener {

	@Override
	public Object beforeFieldInsert(JFishProperty field, Object value) {
		return value;
	}

	@Override
	public Object beforeFieldUpdate(JFishProperty field, Object value) {
		return value;
	}

}
