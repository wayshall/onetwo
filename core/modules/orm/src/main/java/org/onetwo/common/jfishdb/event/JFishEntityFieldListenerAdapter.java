package org.onetwo.common.jfishdb.event;

public class JFishEntityFieldListenerAdapter implements JFishEntityFieldListener {

	@Override
	public Object beforeFieldInsert(String fieldName, Object value) {
		return value;
	}

	@Override
	public Object beforeFieldUpdate(String fieldName, Object value) {
		return value;
	}

}
