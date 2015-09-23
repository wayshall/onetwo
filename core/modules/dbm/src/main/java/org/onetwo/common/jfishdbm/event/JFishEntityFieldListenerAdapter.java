package org.onetwo.common.jfishdbm.event;

import org.onetwo.common.utils.JFishProperty;

public class JFishEntityFieldListenerAdapter implements DbmEntityFieldListener {

	@Override
	public Object beforeFieldInsert(JFishProperty field, Object value) {
		return value;
	}

	@Override
	public Object beforeFieldUpdate(JFishProperty field, Object value) {
		return value;
	}

}
