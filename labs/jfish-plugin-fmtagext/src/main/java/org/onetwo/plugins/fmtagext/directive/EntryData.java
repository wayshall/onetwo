package org.onetwo.plugins.fmtagext.directive;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EntryData implements Serializable {
	
	private Object entity;

	private String idName;
	private Object idValue;
	
	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public Object getIdValue() {
		return idValue;
	}
	public void setIdValue(Object idValue) {
		this.idValue = idValue;
	}

}
